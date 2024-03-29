package com.rectle.team;

import com.google.cloud.storage.BlobId;
import com.rectle.exception.BusinessException;
import com.rectle.file.FilesService;
import com.rectle.team.dto.CreateTeamDto;
import com.rectle.team.model.Team;
import com.rectle.user.UserService;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
	private final TeamRepository teamRepository;
	private final UserService userService;
	private final FilesService filesService;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.images}")
	private String bucketImagesFolder;

	public Team getTeamById(Long id) {
		return teamRepository.findById(id).orElseThrow(
				() -> new BusinessException("There is no team with id: " + id, HttpStatus.NOT_FOUND)
		);
	}

	public Set<Team> getAllTeamsByUserId(Long userId) {
		User user = userService.getUserById(userId);
		return user.getTeams();
	}

	public List<Team> getAllTeamsInWhichUserIsNotParticipant(Long userId) {
		User user = userService.getUserById(userId);
		List<Team> teams = teamRepository.findAll();
		return teams
				.stream()
				.filter(team -> !user.getTeams().contains(team))
				.collect(Collectors.toList());
	}

	public Set<User> getAllUsers(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		return team.getUsers();
	}

	public List<User> getAllUsersThatWantsToJoin(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		String[] usersIds = getUsersIdsThatWantsToJoinTeam(team.getId());
		if (usersIds == null) {
			return null;
		}
		return Arrays.stream(usersIds).map(userId -> userService.getUserById(Long.valueOf(userId))).collect(Collectors.toList());
	}

	public List<User> getAllInvitedUsers(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		String[] usersIds = getUsersIdsThatWereInvited(team.getId());
		if (usersIds == null) {
			return null;
		}
		return Arrays.stream(usersIds).map(userId -> userService.getUserById(Long.valueOf(userId))).collect(Collectors.toList());
	}

	public Team createNew(CreateTeamDto createTeamDto) {
		teamRepository.findTeamByName(createTeamDto.getName()).ifPresent(t -> {
			throw new BusinessException("There is already team with name: " + t.getName(), HttpStatus.CONFLICT);
		});
		Team team = Team.builder()
				.name(createTeamDto.getName())
				.build();
		if (createTeamDto.getLogo() != null && !createTeamDto.getLogo().isEmpty()) {
			team = teamRepository.save(team);
			BlobId blobId = BlobId.of(bucketName, MessageFormat.format("{0}TeamId:{1}", bucketImagesFolder, team.getId()));
			String logoUrl = filesService.uploadImageToStorage(blobId, createTeamDto.getLogo());
			team.setLogoUrl(logoUrl);
		}

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
		team = removeUserFromPendingInvites(teamId, userId);
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

	public Team addUserToAwaitingJoiners(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		User user = userService.getUserById(userId);
		if (team.getJoinRequests() == null || team.getJoinRequests().equals("")) {
			team.setJoinRequests(userId + ",");
			return teamRepository.save(team);
		}
		boolean alreadyRequested = Arrays
				.stream(team.getJoinRequests().split(","))
				.anyMatch(id -> id.equals(user.getId().toString()));
		if (alreadyRequested) {
			throw new BusinessException("User: " + userId + " already requested for joining..", HttpStatus.CONFLICT);
		}
		team.setJoinRequests(team.getJoinRequests() + userId + ",");
		return teamRepository.save(team);
	}

	public Team removeUserFromAwaitingJoiners(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		if (team.getJoinRequests() == null || team.getJoinRequests().equals("")) {
			team.setJoinRequests("");
			return teamRepository.save(team);
		}
		List<String> awaitingJoiners = new ArrayList<>(List.of(getUsersIdsThatWantsToJoinTeam(teamId)));
		if (awaitingJoiners.contains(userId.toString())) {
			awaitingJoiners.remove(userId.toString());
			team.setJoinRequests(adjustJoinInviteString(awaitingJoiners.toString()));
			return teamRepository.save(team);
		}
		return team;
	}

	public String[] getUsersIdsThatWantsToJoinTeam(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		if (team.getJoinRequests() == null || team.getJoinRequests().equals("")) {
			return new String[]{};
		}
		return team.getJoinRequests().split(",");
	}

	public String[] getUsersIdsThatWereInvited(Long teamId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		if (team.getPendingInvites() == null || team.getPendingInvites().equals("")) {
			return new String[]{};
		}
		return team.getPendingInvites().split(",");
	}

	public Team addUserToInvitedUsers(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		User user = userService.getUserById(userId);
		if (team.getPendingInvites() == null || team.getPendingInvites().equals("")) {
			team.setPendingInvites(userId + ",");
			return teamRepository.save(team);
		}
		boolean alreadyInvited = Arrays
				.stream(team.getPendingInvites().split(","))
				.anyMatch(id -> id.equals(user.getId().toString()));
		if (alreadyInvited) {
			throw new BusinessException("User: " + userId + " already invited to team..", HttpStatus.CONFLICT);
		}
		team.setPendingInvites(team.getPendingInvites() + userId + ",");
		return teamRepository.save(team);
	}

	public Team removeUserFromPendingInvites(Long teamId, Long userId) {
		Team team = teamRepository.findById(teamId).orElseThrow(
				() -> new BusinessException("There is no team with id: " + teamId, HttpStatus.NOT_FOUND)
		);
		if (team.getPendingInvites() == null || team.getPendingInvites().equals("")) {
			team.setPendingInvites("");
			return teamRepository.save(team);
		}
		List<String> pendingInvites = new ArrayList<>(List.of(getUsersIdsThatWereInvited(teamId)));
		if (pendingInvites.contains(userId.toString())) {
			pendingInvites.remove(userId.toString());
			team.setPendingInvites(adjustJoinInviteString(pendingInvites.toString()));
			return teamRepository.save(team);
		}
		return team;
	}

	private String adjustJoinInviteString(String joinOrInviteString) {
		return joinOrInviteString.replace("[", "").replace("]", "").replace(" ", "");
	}

	public List<Team> getAllTeams() {
		return teamRepository.findAll();
	}
}
