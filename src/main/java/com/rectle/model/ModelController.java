package com.rectle.model;

import com.rectle.file.FilesUtils;
import com.rectle.model.dto.CreateModelDto;
import com.rectle.model.entity.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.url}/models")
public class ModelController {
	private final ModelService modelService;

	@PostMapping
	public ResponseEntity<Model> createNewModel(@RequestBody CreateModelDto createModelDto) {
		if (createModelDto == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		Model model = modelService.createNew(createModelDto);
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

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

	@PutMapping("/{modelId}")
	public ResponseEntity<Long> compileModel(@PathVariable Long modelId) {
		Long compilationId = modelService.compileModel(modelId);
		return new ResponseEntity<>(compilationId, HttpStatus.OK);
	}
}
