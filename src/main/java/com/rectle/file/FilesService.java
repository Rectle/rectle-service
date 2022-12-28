package com.rectle.file;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Slf4j
@Service
public class FilesService {
	private final Storage storage;
	private final FilesFeignClient filesFeignClient;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.folder}")
	private String bucketFolder;

	public void uploadFileToCloudStorage(String fileName, MultipartFile multipartFile) {
		BlobId blobId = BlobId.of(bucketName, bucketFolder + "/" + fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		try {
			byte[] data = multipartFile.getBytes();
			storage.create(blobInfo, data);
		} catch (IOException e) {
			log.warn("There was a problem with converting file", e);
			throw new RuntimeException("There was a problem with converting file", e);
		}
	}

	public void requestForCompilingFile() {
		filesFeignClient.postForCompileFile("project1.zip");
	}

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
