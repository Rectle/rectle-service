package com.rectle.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api.url}/compilations")
public class CompilationController {
	private final CompilationService compilationService;

	@GetMapping("/{compilationId}/logs")
	public ResponseEntity<List<String>> getLogs(@PathVariable Long compilationId) {
		List<String> logs = compilationService.getLogsTextByCompilationId(compilationId);
		if(logs == null || logs.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}

	@GetMapping("/{compilationId}/runner")
	public ResponseEntity<String> getRunnerUrl(@PathVariable Long compilationId) {
		String runner = compilationService.getRunnerUrlByCompilationId(compilationId);
		if(runner == null || runner.equals("")) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(runner, HttpStatus.OK);
	}

	@PutMapping("/{compilationId}/logs")
	public ResponseEntity<Void> addLogs(@PathVariable Long compilationId, @RequestParam ArrayList<String> logs) {
		compilationService.addNewLogs(logs, compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(path = "/{compilationId}/runner", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> addRunnerUrl(@PathVariable Long compilationId, @RequestParam String url) {
		compilationService.addRunnerUrl(url, compilationId);
		Stream<String> stream = Stream.<String>builder()
				.add(url)
				.build();
		return Flux.fromStream(stream).retry(2);
	}

}
