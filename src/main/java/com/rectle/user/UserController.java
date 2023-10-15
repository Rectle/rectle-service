package com.rectle.user;

import com.rectle.exception.BusinessException;
import com.rectle.user.dto.ResponseCreateUserDto;
import com.rectle.user.dto.UserDto;
import com.rectle.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/${api.url}/users")
@RestController
public class UserController {
	private final UserDtoMapper userDtoMapper;
	private final UserService userService;

	@Operation(summary = "createOrLoginUser")
	@PostMapping
	public ResponseEntity<ResponseCreateUserDto> createOrLoginUser(@RequestBody UserDto userDto) {
		User user = userService.createUser(userDtoMapper.userDtoToUser(userDto));
		return new ResponseEntity<>(userDtoMapper.userToResponseCreateUserDto(user), HttpStatus.CREATED);
	}

	@Operation(summary = "getAllUsers")
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		if (users.isEmpty()) {
			throw new BusinessException("Nothing to show", HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(userDtoMapper.usersToUsersDto(users), HttpStatus.OK);
	}

	@Operation(summary = "getUserById")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		return new ResponseEntity<>(userDtoMapper.userToUserDto(user), HttpStatus.OK);
	}

	@Operation(summary = "getUserByEmail")
	@GetMapping("/email")
	public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Operation(summary = "changeUserName")
	@PutMapping("/{userId}")
	public ResponseEntity<User> changeUserName(@PathVariable Long userId, @RequestParam String name) {
		User user = userService.getUserById(userId);
		return new ResponseEntity<>(userService.changeUserName(user, name), HttpStatus.OK);
	}

}
