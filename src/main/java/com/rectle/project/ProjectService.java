package com.rectle.project;

import com.google.cloud.storage.BlobId;
import com.rectle.compilation.CompilationService;
import com.rectle.compilation.model.Compilation;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesService;
import com.rectle.project.dto.ProjectToCompileDto;
import com.rectle.project.model.Project;
import com.rectle.team.TeamService;
import com.rectle.team.model.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		projectRepository.findProjectByNameAndTeam(project.getName(), project.getTeam()).ifPresent(p -> {
			throw new BusinessException("Project with name: " + p.getName() + " already exists for team: " + p.getTeam().getName(),
					HttpStatus.CONFLICT);
		});
		return projectRepository.save(project);
	}

	public Project uploadProjectToCloudStorage(String fileName, Long teamId, MultipartFile multipartFile) {
		Team team = teamService.getTeamById(teamId);
		Project project = Project.builder()
				.name(fileName)
				.team(team)
				.build();
		project = createNewProject(project);

		BlobId blobId = BlobId.of(bucketName, bucketFolder + "/" + project.getId());
		filesService.uploadZipFileToStorage(blobId, multipartFile);
		return project;
	}

//	public String requestForCompilingProject(Project project) {
//		//Compilation compilation = compilationService.createCompilationByProject(project);
//
//		ProjectToCompileDto projectToCompileDto = ProjectToCompileDto.builder()
//				//.compilationId(compilation.getId().toString())
//				.task(project.getId().toString())
//				.build();
//		filesService.compileProject(projectToCompileDto);
//
//		return compilation.getId().toString();
//	}

	public Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId).orElseThrow(
				() -> new BusinessException("Project with id: " + projectId + " not found", HttpStatus.NOT_FOUND)
		);
	}
}
