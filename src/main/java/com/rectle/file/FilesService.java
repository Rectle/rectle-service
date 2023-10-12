package com.rectle.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.rectle.model.dto.ModelToCompileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

	public void compileModel(ModelToCompileDto modelToCompileDto) {
		filesFeignClient.postForCompileModel(modelToCompileDto);
	}

}
