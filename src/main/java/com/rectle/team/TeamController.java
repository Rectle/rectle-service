package com.rectle.team;

import com.rectle.team.model.Team;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.url}/teams")
public class TeamController {
	private final TeamService teamService;

	@GetMapping("/{teamId}")
	public ResponseEntity<Set<User>> getAllUsersByTeamId(@PathVariable Long teamId) {
		Set<User> users = teamService.getAllUsers(teamId);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Team> getTeamByName(@RequestParam String name) {
		Team team = teamService.getTeamByName(name);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Team> createNewTeam(@RequestBody CreateTeamDto teamDto) {
		if (teamDto == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		Team team = teamService.createNew(teamDto);
		return new ResponseEntity<>(team, HttpStatus.CREATED);
	}

	@PutMapping("/{teamId}/user/{userId}")
	public ResponseEntity<Team> addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.addUserToTeam(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}

	@DeleteMapping("/{teamId}/user/{userId}")
	public ResponseEntity<Team> removeUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
		Team team = teamService.removeUserFromTeam(teamId, userId);
		return new ResponseEntity<>(team, HttpStatus.OK);
	}
}
