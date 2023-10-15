package com.rectle.project;

import com.rectle.file.FilesUtils;
import com.rectle.model.dto.ModelWithCompilationDto;
import com.rectle.project.dto.CreateProjectDto;
import com.rectle.project.dto.UploadedProjectDto;
import com.rectle.project.model.Project;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/${api.url}/projects")
@RestController
public class ProjectController {
	private final ProjectService projectService;
	private final ProjectDtoMapper projectDtoMapper;

	@Operation(summary = "upload new Project")
	@PostMapping("/{projectId}")
	public ResponseEntity<UploadedProjectDto> uploadProject(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long projectId) {
		if (!multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
			Project project = projectService.findProjectById(projectId);
			projectService.uploadProjectToCloudStorage(multipartFile, project);
			log.info(FilesUtils.SUCCESSFULLY_UPLOADED_MSG);
			return new ResponseEntity<>(projectDtoMapper.projectToUploadedProjectDto(project), HttpStatus.CREATED);
		}
		log.warn(FilesUtils.EMPTY_FILE_MSG);
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "create new Project")
	@PostMapping
	public ResponseEntity<Project> createProject(@ModelAttribute CreateProjectDto createProjectDto) {
		Project project = projectService.createProject(createProjectDto);
		return new ResponseEntity<>(project, HttpStatus.CREATED);
	}

	@Operation(summary = "getAll Projects")
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> projects = projectService.collectAll();
		if (projects == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	@Operation(summary = "getProjectsForSpecificUser")
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<Project>> getProjectsForSpecificUser(@PathVariable Long userId) {
		List<Project> projectsForSpecificUser = projectService.collectAllProjectsForSpecificUser(userId);
		return new ResponseEntity<>(projectsForSpecificUser, HttpStatus.OK);
	}

	@Operation(summary = "getProjectById")
	@GetMapping("/{projectId}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
		Project project = projectService.findProjectById(projectId);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@Operation(summary = "getModelsWithCompilationsByProjectId")
	@GetMapping("/{projectId}/models-compilations")
	public ResponseEntity<List<ModelWithCompilationDto>> getModelsWithCompilationsByProjectId(@PathVariable Long projectId) {
		Project project = projectService.findProjectById(projectId);
		List<ModelWithCompilationDto> modelsWithCompilations = projectService.getModelsWithCompilationsByProject(project);
		if (modelsWithCompilations == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(modelsWithCompilations, HttpStatus.OK);
	}

	@Operation(summary = "updateProjectTags")
	@PutMapping("/{projectId}")
	public ResponseEntity<Project> updateProjectTags(@PathVariable Long projectId, @RequestParam String newTags) {
		Project project = projectService.findProjectById(projectId);
		return new ResponseEntity<>(projectService.updateProjectTags(project, newTags), HttpStatus.OK);
	}

	@Operation(summary = "deleteProjectById")
	@DeleteMapping("/{projectId}")
	public ResponseEntity<Boolean> deleteProjectById(@PathVariable Long projectId) {
		projectService.deleteProject(projectId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
