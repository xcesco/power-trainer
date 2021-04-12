package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Misuration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Misuration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisurationRepository extends JpaRepository<Misuration, Long>, JpaSpecificationExecutor<Misuration> {}
