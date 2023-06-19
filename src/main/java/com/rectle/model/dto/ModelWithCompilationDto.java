package com.rectle.model.dto;

import com.rectle.compilation.CompilationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelWithCompilationDto {
	private String modelName;
	private CompilationStatus status;
	private Long compilationId;
}
