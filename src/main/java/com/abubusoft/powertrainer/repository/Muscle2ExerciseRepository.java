package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Muscle2Exercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Muscle2ExerciseRepository extends JpaRepository<Muscle2Exercise, Long>, JpaSpecificationExecutor<Muscle2Exercise> {}
