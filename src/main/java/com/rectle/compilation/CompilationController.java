package com.rectle.compilation;

import com.rectle.compilation.dto.LogsDto;
import com.rectle.compilation.model.Compilation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api.url}/compilations")
@OpenAPIDefinition(info = @Info(
		title = "Rectle REST API",
		version = "1.0.0",
		description = "Operations with Rectle REST API"
))
public class CompilationController {
	private final CompilationService compilationService;

	@Operation(summary = "Get logs by compilationId")
	@GetMapping("/{compilationId}/logs")
	public ResponseEntity<List<String>> getLogs(@PathVariable Long compilationId) {
		List<String> logs = compilationService.getLogsTextByCompilationId(compilationId);
		if (logs == null || logs.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@Operation(summary = "Get runnerUrl by compilationId")
	@GetMapping("/{compilationId}/runner")
	public ResponseEntity<String> getRunnerUrl(@PathVariable Long compilationId) {
		String runner = compilationService.getRunnerUrlByCompilationId(compilationId);
		if (runner == null || runner.equals("")) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(runner, HttpStatus.OK);
	}

	@Operation(summary = "Get all compilations by modelId")
	@GetMapping("/models/{modelId}")
	public ResponseEntity<List<Compilation>> getAllCompilationsByModelId(@PathVariable Long modelId) {
		List<Compilation> compilations = compilationService.getAllCompilationsByModelId(modelId);
		if (compilations == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(compilations, HttpStatus.OK);
	}

	@Operation(summary = "Add logs by compilationId")
	@PostMapping("/{compilationId}/logs")
	public ResponseEntity<Void> addLogs(@PathVariable Long compilationId, @RequestBody LogsDto logs) {
		compilationService.addNewLogs(logs.getLogs(), compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Add runnerUrl by compilationId")
	@PostMapping("/{compilationId}/runner")
	public ResponseEntity<Void> addRunnerUrl(@PathVariable Long compilationId, @RequestBody String url) {
		compilationService.addRunnerUrl(url, compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Add score compilationId")
	@PostMapping("/{compilationId}/score")
	public ResponseEntity<Void> addScore(@PathVariable Long compilationId, @RequestBody Integer score) {
		compilationService.addScore(compilationId, score);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
