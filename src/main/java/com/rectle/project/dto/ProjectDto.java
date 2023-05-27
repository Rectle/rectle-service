package com.rectle.project.dto;

import com.rectle.model.dto.ModelDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Setter
@Getter
public class ProjectDto {
	private Timestamp createDate;
	private String name;
	private Set<ModelDto> modelsDto;
}
