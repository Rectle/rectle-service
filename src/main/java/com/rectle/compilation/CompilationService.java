package com.rectle.compilation;

import com.rectle.compilation.model.Compilation;
import com.rectle.compilation.model.Log;
import com.rectle.exception.BusinessException;
import com.rectle.project.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {
	private final CompilationRepository compilationRepository;
	private final LogRepository logRepository;

	public Compilation createCompilationByProject(Project project) {
		Compilation compilation = Compilation.builder()
				.project(project)
				.user(project.getUser())
				.build();
		return compilationRepository.save(compilation);
	}

	public List<String> getLogsTextByCompilationId(String compilationId) {
		Compilation compilation = compilationRepository.findById(Long.parseLong(compilationId)).orElseThrow(
				() -> new BusinessException("There is no compilation with ID: " + compilationId, HttpStatus.NOT_FOUND)
		);

		return compilation.getLogs().stream().map(Log::getText).toList();
	}

	@Transactional
	public void addNewLogs(List<String> messages, String compilationId) {
		Compilation compilation = compilationRepository.findById(Long.parseLong(compilationId)).orElseThrow(
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
}