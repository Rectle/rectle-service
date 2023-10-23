package com.rectle.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompilationCompetitionDto {
	private Integer score;
	private Timestamp createDate;
}
