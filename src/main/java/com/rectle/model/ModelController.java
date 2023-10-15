package com.rectle.model;

import com.rectle.file.FilesUtils;
import com.rectle.model.dto.CreateModelDto;
import com.rectle.model.entity.Model;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.url}/models")
public class ModelController {
	private final ModelService modelService;

	@Operation(summary = "Create new Model")
	@PostMapping
	public ResponseEntity<Model> createNewModel(@RequestBody CreateModelDto createModelDto) {
		if (createModelDto == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		Model model = modelService.createNew(createModelDto);
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@Operation(summary = "upload Model")
	@PostMapping("/{modelId}")
	public ResponseEntity<Model> uploadModel(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long modelId) {
		if (!multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
			Model model = modelService.uploadModel(modelId, multipartFile);
			log.info(FilesUtils.SUCCESSFULLY_UPLOADED_MSG);
			return new ResponseEntity<>(model, HttpStatus.CREATED);
		}
		log.warn(FilesUtils.EMPTY_FILE_MSG);
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "compile Model")
	@PutMapping("/{modelId}")
	public ResponseEntity<Long> compileModel(@PathVariable Long modelId) {
		Long compilationId = modelService.compileModel(modelId);
		return new ResponseEntity<>(compilationId, HttpStatus.OK);
	}

	@Operation(summary = "getAllModels")
	@GetMapping
	public ResponseEntity<List<Model>> getAllModels() {
		List<Model> models = modelService.getAll();
		if (models == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(models, HttpStatus.OK);
	}

	@Operation(summary = "getAllModelsByUserAndProjectId")
	@GetMapping("/projects/{projectId}/users/{userId}")
	public ResponseEntity<List<Model>> getAllModelsByUserAndProjectId(@PathVariable Long projectId, @PathVariable Long userId) {
		List<Model> models = modelService.getAllModelsByUserAndProject(userId, projectId);
		if (models == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(models, HttpStatus.OK);
	}

}
