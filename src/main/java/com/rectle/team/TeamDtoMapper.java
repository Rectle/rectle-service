package com.rectle.team;

import com.rectle.team.dto.AllTeamsDto;
import com.rectle.team.dto.JoinInviteTeamDto;
import com.rectle.team.model.Team;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TeamDtoMapper {

	Set<AllTeamsDto> teamsToAllTeamsDtos(Set<Team> teams);

	JoinInviteTeamDto teamToJoinInviteTeamDto(Team team);
}
