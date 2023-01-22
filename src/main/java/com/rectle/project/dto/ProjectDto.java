package com.rectle.project.dto;

import com.rectle.model.ModelDto;
import com.rectle.score.ScoreDto;
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
	private Set<ScoreDto> scoresDto;
}
