package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.WorkoutStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutStepRepository extends JpaRepository<WorkoutStep, Long>, JpaSpecificationExecutor<WorkoutStep> {}
