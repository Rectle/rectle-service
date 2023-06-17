package com.rectle.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelToCompileDto {
	private String modelId;
	private String projectId;
	private String compilationId;
}
