package com.rectle.model;

import com.rectle.score.ScoreDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ModelDto {
	private Set<ScoreDto> scoresDto;
	private String name;
}
