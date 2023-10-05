package com.rectle.team.dto;

import com.rectle.user.dto.GroupsUserDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class AllTeamsDto {
	private Long id;
	private String name;
	private Timestamp createDate;
	private String logoUrl;
	private List<GroupsUserDto> users;
}
