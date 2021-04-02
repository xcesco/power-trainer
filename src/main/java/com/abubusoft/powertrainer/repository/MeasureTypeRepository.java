package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.MeasureType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MeasureType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasureTypeRepository extends JpaRepository<MeasureType, Long>, JpaSpecificationExecutor<MeasureType> {}
