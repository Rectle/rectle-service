package com.rectle.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCreateUserDto {
	private Long id;
	private String provider;
	private String email;
}
