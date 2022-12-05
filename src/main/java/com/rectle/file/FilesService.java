package com.rectle.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FilesService {


	public void saveFile(String fileName, MultipartFile multipartFile) {
		Path savePath = Path.of(FilesUtils.SAVED_FILES_DIRECTORY);
		if (!Files.exists(savePath)) {
			try {
				Files.createDirectories(savePath);
			} catch (IOException e) {
				log.warn("There was a problem with creating files directory", e);
				throw new RuntimeException(e);
			}
		}
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = savePath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.warn("There was a problem with saving file", e);
			throw new RuntimeException(e);
		}
	}
}
