package com.rectle.project;

import com.google.cloud.storage.BlobId;
import com.rectle.compilation.CompilationService;
import com.rectle.compilation.model.Compilation;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesService;
import com.rectle.model.dto.ModelWithCompilationDto;
import com.rectle.model.entity.Model;
import com.rectle.project.dto.CreateProjectDto;
import com.rectle.project.model.Project;
import com.rectle.team.TeamService;
import com.rectle.team.model.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final CompilationService compilationService;
	private final TeamService teamService;
	private final FilesService filesService;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.folder}")
	private String bucketFolder;

	@Value("${bucket.images}")
	private String bucketImagesFolder;

	public Project createNewProject(Project project) {
		return projectRepository.save(project);
	}

	public void deleteProject(Long projectId) {
		Project project = findProjectById(projectId);
		BlobId blobId = BlobId.of(bucketName, bucketFolder + project.getId() + "/code.zip");
		if (filesService.deleteZipFileFromStorage(blobId)) {
			projectRepository.delete(project);
		} else {
			log.warn(MessageFormat.format("Delete project {0} from storage FAILED, retrying..", project.getId()));
			if (filesService.deleteZipFileFromStorage(blobId)) {
				projectRepository.delete(project);
				return;
			}
			throw new BusinessException(MessageFormat
					.format("Something went wrong while trying to delete project: {0} files..", project.getId()),
					HttpStatus.BAD_REQUEST);
		}
	}

	public List<Project> collectAll() {
		return projectRepository.findAll();
	}

	public List<ModelWithCompilationDto> getModelsWithCompilationsByProject(Project project) {
		Set<Model> models = project.getModels();
		if (models == null || models.isEmpty()) {
			return null;
		}
		Set<Compilation> compilations = new HashSet<>();
		models.forEach(model -> compilations.addAll(model.getCompilations()));
		return compilations
				.stream()
				.filter(Objects::nonNull)
				.map(compilation -> ModelWithCompilationDto
						.builder()
						.compilationId(compilation.getId())
						.modelName(compilation.getModel().getName())
						.status(compilationService.getCompilationStatus(compilation))
						.build())
				.collect(Collectors.toList());
	}

	public Project createProject(CreateProjectDto createProjectDto) {
		Team team = teamService.getTeamById(createProjectDto.getTeamId());
		Project project = Project.builder()
				.name(createProjectDto.getName())
				.description(createProjectDto.getDescription())
				.team(team)
				.tags(createProjectDto.getTags())
				.build();
		if (createProjectDto.getLogo() != null && !createProjectDto.getLogo().isEmpty()) {
			project = createNewProject(project);
			BlobId blobId = BlobId.of(bucketName, bucketImagesFolder + project.getId());
			String logoUrl = filesService.uploadImageToStorage(blobId, createProjectDto.getLogo());
			project.setLogoUrl(logoUrl);
		}
		return createNewProject(project);
	}

	public Project updateProjectTags(Project project, String newTags) {
		if (newTags == null) {
			throw new BusinessException("New tags are empty", HttpStatus.NO_CONTENT);
		}
		project.setTags(newTags);
		return projectRepository.save(project);
	}

	public void uploadProjectToCloudStorage(MultipartFile multipartFile, Project project) {
		BlobId blobId = BlobId.of(bucketName, bucketFolder + project.getId() + "/code.zip");
		filesService.uploadZipFileToStorage(blobId, multipartFile);
	}

	public Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId).orElseThrow(
				() -> new BusinessException("Project with id: " + projectId + " not found", HttpStatus.NOT_FOUND)
		);
	}

	public List<Project> collectAllProjectsForSpecificUser(Long userId) {
		Set<Team> teamsForSpecificUser = teamService.getAllTeamsByUserId(userId);
		return projectRepository
				.findAll()
				.stream()
				.filter(project -> teamsForSpecificUser.contains(project.getTeam()))
				.toList();
	}
}
