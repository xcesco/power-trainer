package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutSheetExercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutSheetExerciseRepository
    extends JpaRepository<WorkoutSheetExercise, Long>, JpaSpecificationExecutor<WorkoutSheetExercise> {}
