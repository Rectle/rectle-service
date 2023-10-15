package com.rectle.model;

import com.google.cloud.storage.BlobId;
import com.rectle.compilation.CompilationService;
import com.rectle.compilation.model.Compilation;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesService;
import com.rectle.model.dto.CreateModelDto;
import com.rectle.model.dto.ModelToCompileDto;
import com.rectle.model.entity.Model;
import com.rectle.project.ProjectService;
import com.rectle.project.model.Project;
import com.rectle.user.UserService;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelService {
	private final ModelRepository modelRepository;
	private final UserService userService;
	private final ProjectService projectService;
	private final FilesService filesService;
	private final CompilationService compilationService;

	@Value("${model.bucket.name}")
	private String bucketName;

	@Value("${model.bucket.folder}")
	private String bucketFolder;

	public Model createNew(CreateModelDto createModelDto) {
		Project project = projectService.findProjectById(createModelDto.getProjectId());
		User user = userService.getUserById(createModelDto.getUserId());

		Model model = Model.builder()
				.name(createModelDto.getName())
				.user(user)
				.project(project)
				.compilations(new HashSet<>())
				.build();
		return modelRepository.save(model);
	}

	public Model uploadModel(Long modelId, MultipartFile multipartFile) {
		Model model = modelRepository.findById(modelId).orElseThrow(() -> new BusinessException(
				"There is no model with id: " + modelId, HttpStatus.NOT_FOUND
		));
		String baseFolder = bucketFolder.replace("#", model.getProject().getId().toString());
		BlobId blobId = BlobId.of(bucketName, baseFolder + modelId + "/model.zip");
		filesService.uploadZipFileToStorage(blobId, multipartFile);
		return model;
	}

	public Long compileModel(Long modelId) {
		Model model = modelRepository.findById(modelId).orElseThrow(() ->
			new BusinessException("There is no model with id: " + modelId, HttpStatus.NOT_FOUND)
		);
		Compilation compilation = compilationService.createCompilationByModel(model);
		ModelToCompileDto modelToCompileDto = ModelToCompileDto.builder()
				.compilationId(compilation.getId().toString())
				.projectId(model.getProject().getId().toString())
				.modelId(modelId.toString())
				.build();
		filesService.compileModel(modelToCompileDto);
		return compilation.getId();
	}

	public List<Model> getAllModelsByUserAndProject(Long userId, Long projectId) {
		User user = userService.getUserById(userId);
		Project project = projectService.findProjectById(projectId);
		Optional<List<Model>> models = modelRepository.findModelsByUserProject(user, project);
		return models.orElse(null);
	}

	public List<Model> getAll() {
		return modelRepository.findAll();
	}
}
