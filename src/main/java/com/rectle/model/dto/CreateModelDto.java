package com.rectle.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateModelDto {
	private Long userId;
	private Long projectId;
	private String name;
}
