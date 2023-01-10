package com.rectle.project;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesFeignClient;
import com.rectle.project.model.Project;
import com.rectle.user.UserService;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final UserService userService;
	private final Storage storage;
	private final FilesFeignClient filesFeignClient;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.folder}")
	private String bucketFolder;

	public Project createNewProject(Project project) {
		projectRepository.findProjectByNameAndUser(project.getName(), project.getUser()).ifPresent(p -> {
			throw new BusinessException("Project with name: " + p.getName() + " already exists for user: " + p.getUser().getId(), HttpStatus.CONFLICT);
		});
		return projectRepository.save(project);
	}

	public void uploadProjectToCloudStorage(String fileName, Long userId, MultipartFile multipartFile) {
		User user = userService.getUserById(userId);
		Project project = Project.builder()
				.user(user)
				.name(fileName)
				.build();
		project = createNewProject(project);

		BlobId blobId = BlobId.of(bucketName, bucketFolder + "/" + project.getId());
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		try {
			byte[] data = multipartFile.getBytes();
			storage.create(blobInfo, data);
		} catch (IOException e) {
			log.warn("There was a problem with converting file", e);
			throw new RuntimeException("There was a problem with converting file", e);
		}
	}

	public void requestForCompilingProject(Long projectId) {
		filesFeignClient.postForCompileFile(projectId.toString());
	}
}
