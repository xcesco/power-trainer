package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Translation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Translation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long>, JpaSpecificationExecutor<Translation> {}
