package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Muscle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Muscle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Long>, JpaSpecificationExecutor<Muscle> {}
