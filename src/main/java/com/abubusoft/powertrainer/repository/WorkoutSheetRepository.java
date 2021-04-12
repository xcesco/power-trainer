package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.WorkoutSheet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkoutSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkoutSheetRepository extends JpaRepository<WorkoutSheet, Long>, JpaSpecificationExecutor<WorkoutSheet> {}
