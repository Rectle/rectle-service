package com.rectle.project.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateProjectDto {
	private String name;
	private String description;
	private String tags;
	private Long teamId;
	private MultipartFile logo;
}