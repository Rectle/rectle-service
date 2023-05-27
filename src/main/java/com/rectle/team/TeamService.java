package com.rectle.team;

import com.rectle.exception.BusinessException;
import com.rectle.team.model.Team;
import com.rectle.user.UserService;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
	private final TeamRepository teamRepository;
	private final UserService userService;

	public Team getTeamByName(String name) {
		return teamRepository.findTeamByName(name).orElseThrow(
				() -> new BusinessException("There is no team with name: " + name, HttpStatus.NOT_FOUND)
		);
	}

	public Team getTeamById(Long id) {
		return teamRepository.findById(id).orElseThrow(
				() -> new BusinessException("There is no team with id: " + id, HttpStatus.NOT_FOUND)
		);
	}

	public Set<User> getAllUsers(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		return team.getUsers();
	}

	public Team createNew(CreateTeamDto createTeamDto) {
		teamRepository.findTeamByName(createTeamDto.getName()).ifPresent(t -> {
			throw new BusinessException("There is already team with name: " + t.getName(), HttpStatus.CONFLICT);
		});
		Team team = Team.builder()
				.name(createTeamDto.getName())
				.build();
		return teamRepository.save(team);
	}

	public Team addUserToTeam(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		User user = userService.getUserById(userId);
		if (team.getUsers().contains(user)) {
			throw new BusinessException(
					"Team with id: " + teamId + " already has user with id: " + userId, HttpStatus.CONFLICT);
		}
		userService.addUserToTeam(user, team);
		return team;
	}

	public Team removeUserFromTeam(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		User user = userService.getUserById(userId);
		if (!team.getUsers().contains(user)) {
			throw new BusinessException(
					"Team with id: " + teamId + " dont have user with id: " + userId, HttpStatus.NOT_FOUND);
		}
		userService.removeUserFromTeam(user, team);
		return team;
	}
}
