package com.rectle.team;

import com.rectle.team.dto.AllTeamsDto;
import com.rectle.team.dto.CreateTeamDto;
import com.rectle.team.model.Team;
import com.rectle.user.UserDtoMapper;
import com.rectle.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.url}/teams")
public class TeamController {
	private final TeamService teamService;
	private final TeamDtoMapper teamDtoMapper;
	private final UserDtoMapper userDtoMapper;

	@Operation(summary = "getAllTeams")
	@GetMapping
	public ResponseEntity<List<Team>> getAllTeams() {
		List<Team> teams = teamService.getAllTeams();
		return new ResponseEntity<>(teams, HttpStatus.OK);
	}

	@Operation(summary = "getAllUsersByTeamId")
	@GetMapping("/{teamId}")
	public ResponseEntity<Set<User>> getAllUsersByTeamId(@PathVariable Long teamId) {
		Set<User> users = teamService.getAllUsers(teamId);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@Operation(summary = "getAllUsersThatWantsToJoinThisTeam")
	@GetMapping("/{teamId}/wants-to-join")
	public ResponseEntity<List<User>> getAllUsersThatWantsToJoinThisTeam(@PathVariable Long teamId) {
		List<User> users = teamService.getAllUsersThatWantsToJoin(teamId);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@Operation(summary = "getAllUsersThatWereInvitedToTeam")
	@GetMapping("/{teamId}/invited")
	public ResponseEntity<List<User>> getAllUsersThatWereInvitedToTeam(@PathVariable Long teamId) {
		List<User> users = teamService.getAllInvitedUsers(teamId);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@Operation(summary = "getAllTeamsUserIsNotParticipant")
	@GetMapping("/users/{userId}/not-in")
	public ResponseEntity<List<Team>> getAllTeamsUserIsNotParticipant(@PathVariable Long userId) {
		List<Team> teams = teamService.getAllTeamsInWhichUserIsNotParticipant(userId);
		return new ResponseEntity<>(teams, HttpStatus.OK);
	}

	@Operation(summary = "getAllTeamsByUserId")
	@GetMapping("/users/{userId}")
	public ResponseEntity<Set<AllTeamsDto>> getAllTeamsByUserId(@PathVariable Long userId) {
		Set<Team> teams = teamService.getAllTeamsByUserId(userId);
		Set<AllTeamsDto> allTeamsDtos = teamDtoMapper.teamsToAllTeamsDtos(teams);
		allTeamsDtos.forEach(teamDto -> {
			teamDto.setInvited(teamService.getUsersIdsThatWereInvited(teamDto.getId()));
			teamDto.setWantToJoin(teamService.getUsersIdsThatWantsToJoinTeam(teamDto.getId()));
		});
		return new ResponseEntity<>(allTeamsDtos, HttpStatus.OK);
	}

	@Operation(summary = "getTeamByName")
	@GetMapping
	public ResponseEntity<Team> getTeamByName(@RequestParam String name) {
		Team team = teamService.getTeamByName(name);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "createNewTeam")
	@PostMapping
	public ResponseEntity<Team> createNewTeam(@ModelAttribute CreateTeamDto teamDto) {
		if (teamDto == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		Team team = teamService.createNew(teamDto);
		return new ResponseEntity<>(team, HttpStatus.CREATED);
	}

	@Operation(summary = "addUserToTeam")
	@PutMapping("/{teamId}/user/{userId}")
	public ResponseEntity<Team> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.addUserToTeam(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "removeUserFromTeam")
	@DeleteMapping("/{teamId}/user/{userId}")
	public ResponseEntity<Team> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.removeUserFromTeam(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "userWantsToJoin")
	@PutMapping("/{teamId}/user/{userId}/join-request")
	public ResponseEntity<Team> addUserToAwaitingJoiners(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.addUserToAwaitingJoiners(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "removeUserFromAwaitingJoiners")
	@DeleteMapping("/{teamId}/user/{userId}/join-request")
	public ResponseEntity<Team> removeUserFromAwaitingJoiners(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.removeUserFromAwaitingJoiners(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "addInvitedUser")
	@PutMapping("/{teamId}/user/{userId}/invite")
	public ResponseEntity<Team> addUserToInvitedUsers(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.addUserToInvitedUsers(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@Operation(summary = "removeUserFromInvitedUsers")
	@DeleteMapping("/{teamId}/user/{userId}/invite")
	public ResponseEntity<Team> removeUserFromInvitedUsers(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.removeUserFromPendingInvites(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}
}
