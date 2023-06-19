package com.rectle.project;

import com.google.cloud.storage.BlobId;
import com.rectle.compilation.CompilationService;
import com.rectle.compilation.model.Compilation;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesService;
import com.rectle.model.dto.ModelWithCompilationDto;
import com.rectle.model.entity.Model;
import com.rectle.project.model.Project;
import com.rectle.team.TeamService;
import com.rectle.team.model.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	public Project createNewProject(Project project) {
		return projectRepository.save(project);
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

	public Project uploadProjectToCloudStorage(String fileName, Long teamId, MultipartFile multipartFile) {
		Team team = teamService.getTeamById(teamId);
		Project project = Project.builder()
				.name(fileName)
				.team(team)
				.build();
		project = createNewProject(project);

		BlobId blobId = BlobId.of(bucketName, bucketFolder + project.getId() + "/code");
		filesService.uploadZipFileToStorage(blobId, multipartFile);
		return project;
	}

	public Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId).orElseThrow(
				() -> new BusinessException("Project with id: " + projectId + " not found", HttpStatus.NOT_FOUND)
		);
	}
}
