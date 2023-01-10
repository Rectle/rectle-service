package com.rectle.project;

import com.rectle.file.FilesUtils;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RequestMapping("/${api.url}/projects")
@RestController
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/{userId}")
	public ResponseEntity<String> uploadProject(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long userId) {
		if (!multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			projectService.uploadProjectToCloudStorage(fileName, userId, multipartFile);
			return new ResponseEntity<>(FilesUtils.SUCCESSFULLY_UPLOADED_MSG, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(FilesUtils.EMPTY_FILE_MSG, HttpStatus.BAD_REQUEST);
	}

	@PutMapping
	public ResponseEntity<String> compileProject(@RequestParam("projectId") Long projectId) {
		projectService.requestForCompilingProject(projectId);
		return new ResponseEntity<>(FilesUtils.SUCCESSFULLY_REQUESTED_FOR_COMPILE, HttpStatus.OK);
	}
}
