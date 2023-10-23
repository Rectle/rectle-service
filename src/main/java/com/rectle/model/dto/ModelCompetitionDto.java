package com.rectle.model.dto;

import com.rectle.compilation.dto.CompilationCompetitionDto;
import com.rectle.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class ModelCompetitionDto {
	private String name;
	private Timestamp createDate;
	private String resourceUrl;
	private List<CompilationCompetitionDto> compilations;
	private UserDto user;
}
