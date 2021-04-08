package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.ExerciseTool;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseTool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseToolRepository extends JpaRepository<ExerciseTool, Long>, JpaSpecificationExecutor<ExerciseTool> {}
