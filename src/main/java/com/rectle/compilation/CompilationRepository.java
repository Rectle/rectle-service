package com.rectle.compilation;

import com.rectle.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

	@Query("SELECT c FROM Compilation c WHERE c.model.id = ?1")
	Optional<List<Compilation>> findCompilationsByModelId(Long modelId);
}
