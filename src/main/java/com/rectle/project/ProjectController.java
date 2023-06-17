package com.rectle.project;

import com.rectle.file.FilesUtils;
import com.rectle.project.dto.UploadedProjectDto;
import com.rectle.project.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/${api.url}/projects")
@RestController
public class ProjectController {
	private final ProjectService projectService;
	private final ProjectDtoMapper projectDtoMapper;

	@PostMapping("/{teamId}")
	public ResponseEntity<UploadedProjectDto> uploadProject(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long teamId) {
		if (!multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			Project project = projectService.uploadProjectToCloudStorage(fileName, teamId, multipartFile);
			log.info(FilesUtils.SUCCESSFULLY_UPLOADED_MSG);
			return new ResponseEntity<>(projectDtoMapper.projectToUploadedProjectDto(project), HttpStatus.CREATED);
		}
		log.warn(FilesUtils.EMPTY_FILE_MSG);
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

//	@PutMapping("/{projectId}")
//	public ResponseEntity<String> compileProject(@PathVariable("projectId") Long projectId) {
//		Project project = projectService.findProjectById(projectId);
//		String compilationId = projectService.requestForCompilingProject(project);
//		return new ResponseEntity<>(compilationId, HttpStatus.OK);
//	}
}
