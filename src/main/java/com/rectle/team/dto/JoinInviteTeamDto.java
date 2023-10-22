package com.rectle.team.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class JoinInviteTeamDto {
	private Long id;
	private String name;
	private Timestamp createDate;
	private String logoUrl;
	private String[] wantToJoin;
	private String[] invited;
}
