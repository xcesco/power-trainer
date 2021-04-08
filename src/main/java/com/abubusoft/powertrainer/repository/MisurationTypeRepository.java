package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.MisurationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MisurationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisurationTypeRepository extends JpaRepository<MisurationType, Long>, JpaSpecificationExecutor<MisurationType> {}
