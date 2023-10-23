package com.rectle.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.rectle.model.dto.ModelToCompileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesService {
	private final Storage storage;
	private final FilesFeignClient filesFeignClient;

	public void uploadZipFileToStorage(BlobId blobId, MultipartFile multipartFile) {
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/x-zip-compressed").build();
		upload(multipartFile, blobInfo);
	}

	public String uploadImageToStorage(BlobId blobId, MultipartFile multipartFile) {
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
		return upload(multipartFile, blobInfo);
	}

	private String upload(MultipartFile multipartFile, BlobInfo blobInfo) {
		try {
			byte[] data = multipartFile.getBytes();
			Blob blob = storage.create(blobInfo, data);
			return blob.getMediaLink();
		} catch (IOException e) {
			log.warn("There was a problem with converting file", e);
			throw new RuntimeException("There was a problem with converting file", e);
		}
	}

	public boolean deleteZipFileFromStorage(BlobId blobId) {
		return storage.delete(blobId);
	}

	public boolean checkIfFileExistsInStorage(String bucketName, String fileName) {
		try {
			Blob blob = storage.get(bucketName, fileName);
			return blob != null;
		} catch (StorageException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}

	public String getFileUrl(String bucketName, String fileName) {
		if (checkIfFileExistsInStorage(bucketName, fileName)) {
			try {
				Blob blob = storage.get(bucketName, fileName);
				return blob.getMediaLink();
			} catch (StorageException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		log.info(MessageFormat.format("File: {0} doesn't exist in storage..", fileName));
		return null;
	}

	public void compileModel(ModelToCompileDto modelToCompileDto) {
		filesFeignClient.postForCompileModel(modelToCompileDto);
	}

}
