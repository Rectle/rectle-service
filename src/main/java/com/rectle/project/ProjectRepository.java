package com.rectle.project;

import com.rectle.project.model.Project;
import com.rectle.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT p FROM Project p WHERE p.name = ?1 AND p.user = ?2")
	Optional<Project> findProjectByNameAndUser(String name, User user);
}
