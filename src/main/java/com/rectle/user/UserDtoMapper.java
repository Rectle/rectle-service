package com.rectle.user;

import com.rectle.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

	UserDto userToUserDto(User user);

	User userDtoToUser(UserDto userDto);

	List<UserDto> usersToUsersDto(List<User> userList);
}
