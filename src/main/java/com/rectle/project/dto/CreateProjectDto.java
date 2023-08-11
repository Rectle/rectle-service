package com.rectle.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectDto {
	private String name;
	private String description;
	private Long teamId;
}