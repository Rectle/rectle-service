package com.rectle.compilation;

import com.rectle.compilation.dto.CompilationCompetitionDto;
import com.rectle.compilation.model.Compilation;
import com.rectle.compilation.model.Log;
import com.rectle.exception.BusinessException;
import com.rectle.model.entity.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
	private final CompilationRepository compilationRepository;
	private final LogRepository logRepository;

	public Compilation createCompilationByModel(Model model) {
		Compilation compilation = Compilation.builder()
				.model(model)
				.score(0)
				.build();
		return compilationRepository.save(compilation);
	}

	public CompilationStatus getCompilationStatus(Compilation compilation) {
		if (compilation.getLogs() != null && !compilation.getLogs().isEmpty()) {
			return CompilationStatus.DONE;
		}
		return CompilationStatus.PENDING;
	}

	public List<String> getLogsTextByCompilationId(Long compilationId) {
		Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);

		return compilation.getLogs()
				.stream()
				.map(Log::getText)
				.toList();
	}

	public String getRunnerUrlByCompilationId(Long compilationId) {
		Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);
		return compilation.getRunnerUrl();
	}

	@Transactional
	public void addNewLogs(List<String> messages, Long compilationId) {
		Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);
		messages.forEach(message -> {
			Log log = Log.builder()
					.text(message)
					.compilation(compilation)
					.build();
			logRepository.save(log);
		});
	}

	public void addRunnerUrl(String runnerUrl, Long compilationId) {
		Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);
		compilation.setRunnerUrl(runnerUrl);
		compilationRepository.save(compilation);
	}

	public void addScore(Long compilationId, Integer score) {
		Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);
		compilation.setScore(score);
		compilationRepository.save(compilation);
	}

	public List<Compilation> getAllCompilationsByModelId(Long modelId) {
		return compilationRepository.findCompilationsByModelId(modelId).orElse(null);
	}

	public List<CompilationCompetitionDto> getAllCompilationsCompetitionByModelId(Long modelId) {
		List<Compilation> compilations = getAllCompilationsByModelId(modelId);
		if (compilations == null) {
			compilations = new ArrayList<>();
		}
		return compilations
				.stream()
				.map(compilation -> new CompilationCompetitionDto(compilation.getScore(), compilation.getCreateDate()))
				.collect(Collectors.toList());
	}
}
