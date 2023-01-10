package com.rectle.user;

import com.rectle.exception.BusinessException;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public User createUser(User user) {
		userRepository.findUserByEmail(user.getEmail()).ifPresent(
				u -> {
					throw new BusinessException("Email " + u.getEmail() + " already exists", HttpStatus.CONFLICT);
				}
		);
		return userRepository.save(user);
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
