package com.rectle.team.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateTeamDto {
	private String name;
	private MultipartFile logo;
}
