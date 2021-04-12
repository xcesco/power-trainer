package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.ExerciseValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseValueRepository extends JpaRepository<ExerciseValue, Long>, JpaSpecificationExecutor<ExerciseValue> {}
