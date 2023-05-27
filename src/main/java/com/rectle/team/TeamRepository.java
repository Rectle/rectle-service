package com.rectle.team;

import com.rectle.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
	@Query("SELECT t FROM Team t WHERE t.name = ?1")
	Optional<Team> findTeamByName(String name);
}
