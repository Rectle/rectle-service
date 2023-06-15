package com.rectle.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api.url}/compilations")
public class CompilationController {
	private final CompilationService compilationService;

	@GetMapping("/{compilationId}/runner")
	public ResponseEntity<String> getRunnerUrl(@PathVariable Long compilationId) {
		String runner = compilationService.getRunnerUrlByCompilationId(compilationId);
		if(runner == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(runner, HttpStatus.OK);
	}

	@PutMapping("/{compilationId}/logs")
	public ResponseEntity<Void> addLogs(@PathVariable Long compilationId, @RequestParam ArrayList<String> logs) {
		compilationService.addNewLogs(logs, compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{compilationId}/runner")
	public ResponseEntity<Void> addRunnerUrl(@PathVariable Long compilationId, @RequestParam String url) {
		compilationService.addRunnerUrl(url, compilationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
