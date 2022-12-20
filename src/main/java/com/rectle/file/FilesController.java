package com.rectle.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/${api.url}/files")
@RestController
public class FilesController {

	private final FilesService filesService;

	@PostMapping
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
		if (!multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			filesService.uploadFileToCloudStorage(fileName, multipartFile);
			return new ResponseEntity<>(FilesUtils.SUCCESSFULLY_UPLOADED_MSG, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(FilesUtils.EMPTY_FILE_MSG, HttpStatus.BAD_REQUEST);
	}
}
