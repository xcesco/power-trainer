package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.MeasureValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MeasureValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasureValueRepository extends JpaRepository<MeasureValue, Long>, JpaSpecificationExecutor<MeasureValue> {}
