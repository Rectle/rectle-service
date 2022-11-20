package com.rectle.user;

import com.rectle.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.login = ?1")
	Optional<User> findUserByLogin(String login);

	@Query("SELECT u FROM User u WHERE u.email = ?1")
	Optional<User> findUserByEmail(String email);
}
