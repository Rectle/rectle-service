package com.rectle.user;

import com.rectle.exception.BusinessException;
import com.rectle.team.model.Team;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public User createUser(User user) {
		Optional<User> alreadyCreatedUser = userRepository.findUserByEmail(user.getEmail());
		if (alreadyCreatedUser.isPresent()) {
			return alreadyCreatedUser.get();
		}
		Team team = Team.builder()
				.name(user.getEmail())
				.users(new HashSet<>())
				.build();
		user.addTeam(team);
		return userRepository.save(user);
	}

	public void addUserToTeam(User user, Team team) {
		user.addTeam(team);
		userRepository.save(user);
	}

	public void removeUserFromTeam(User user, Team team) {
		user.removeTeam(team);
		userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(
				() -> new BusinessException("There is no user with id: " + id, HttpStatus.NOT_FOUND)
		);
	}

	public User getUserByEmail(String email) {
		return userRepository.findUserByEmail(email).orElseThrow(
				() -> new BusinessException("There is no user with email: " + email, HttpStatus.NOT_FOUND)
		);
	}
}
