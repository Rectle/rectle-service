package com.rectle.user;

import com.rectle.user.dto.GroupsUserDto;
import com.rectle.user.dto.ResponseCreateUserDto;
import com.rectle.user.dto.UserDto;
import com.rectle.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

	UserDto userToUserDto(User user);

	User userDtoToUser(UserDto userDto);

	ResponseCreateUserDto userToResponseCreateUserDto(User user);

	List<UserDto> usersToUsersDto(List<User> userList);

	List<GroupsUserDto> usersToGroupsUserDtos(List<User> users);
}
