package com.rectle.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
	private Long id;
	private String provider;
	private String password;
	private String email;
	private String name;
}
