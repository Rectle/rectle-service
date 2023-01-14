package com.rectle.user;

import com.rectle.exception.BusinessException;
import com.rectle.user.dto.ResponseCreateUserDto;
import com.rectle.user.dto.UserDto;
import com.rectle.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/${api.url}/users")
@RestController
public class UserController {
	private final UserDtoMapper userDtoMapper;
	private final UserService userService;

	@PostMapping
	public ResponseEntity<ResponseCreateUserDto> createUser(@RequestBody UserDto userDto) {
		User user = userService.createUser(userDtoMapper.userDtoToUser(userDto));
		return new ResponseEntity<>(userDtoMapper.userToResponseCreateUserDto(user), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		if (users.isEmpty()) {
			throw new BusinessException("Nothing to show", HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(userDtoMapper.usersToUsersDto(users), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		return new ResponseEntity<>(userDtoMapper.userToUserDto(user), HttpStatus.OK);
	}

}
