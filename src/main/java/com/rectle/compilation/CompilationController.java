package com.rectle.compilation;

import com.rectle.compilation.dto.LogsDto;
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
public class CompilationController {
	private final CompilationService compilationService;

	@GetMapping("/{compilationId}/logs")
	public ResponseEntity<List<String>> getLogs(@PathVariable Long compilationId) {
		List<String> logs = compilationService.getLogsTextByCompilationId(compilationId);
		if (logs == null || logs.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@GetMapping("/{compilationId}/runner")
	public ResponseEntity<String> getRunnerUrl(@PathVariable Long compilationId) {
		String runner = compilationService.getRunnerUrlByCompilationId(compilationId);
		if (runner == null || runner.equals("")) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(runner, HttpStatus.OK);
	}

	@PostMapping("/{compilationId}/logs")
	public ResponseEntity<Void> addLogs(@PathVariable Long compilationId, @RequestBody LogsDto logs) {
		compilationService.addNewLogs(logs.getLogs(), compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{compilationId}/runner")
	public ResponseEntity<Void> addRunnerUrl(@PathVariable Long compilationId, @RequestBody String url) {
		compilationService.addRunnerUrl(url, compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{compilationId}/score")
	public ResponseEntity<Void> addScore(@PathVariable Long compilationId, @RequestBody Integer score) {
		compilationService.addScore(compilationId, score);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
