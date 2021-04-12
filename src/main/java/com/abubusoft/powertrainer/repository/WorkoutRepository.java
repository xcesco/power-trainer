package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Workout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Workout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>, JpaSpecificationExecutor<Workout> {}
