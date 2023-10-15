package com.rectle.model;

import com.rectle.model.entity.Model;
import com.rectle.project.model.Project;
import com.rectle.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
	@Query("SELECT m FROM Model m WHERE m.user = ?1 AND m.project = ?2 AND m.name = ?3")
	Optional<Model> findModelByUserProjectName(User user, Project project, String name);

	@Query("SELECT m FROM Model m WHERE m.user = ?1 AND m.project = ?2")
	Optional<List<Model>> findModelsByUserProject(User user, Project project);
}
