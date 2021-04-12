package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.ExerciseResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseResourceRepository extends JpaRepository<ExerciseResource, Long>, JpaSpecificationExecutor<ExerciseResource> {}
