package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.TranslationRepository;
import com.abubusoft.powertrainer.service.criteria.TranslationCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Translation} entities in the database.
 * The main input is a {@link TranslationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Translation} or a {@link Page} of {@link Translation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TranslationQueryService extends QueryService<Translation> {

    private final Logger log = LoggerFactory.getLogger(TranslationQueryService.class);

    private final TranslationRepository translationRepository;

    public TranslationQueryService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    /**
     * Return a {@link List} of {@link Translation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Translation> findByCriteria(TranslationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Translation> specification = createSpecification(criteria);
        return translationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Translation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Translation> findByCriteria(TranslationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Translation> specification = createSpecification(criteria);
        return translationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TranslationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Translation> specification = createSpecification(criteria);
        return translationRepository.count(specification);
    }

    /**
     * Function to convert {@link TranslationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Translation> createSpecification(TranslationCriteria criteria) {
        Specification<Translation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Translation_.id));
            }
            if (criteria.getEntityType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityType(), Translation_.entityType));
            }
            if (criteria.getEntityUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityUuid(), Translation_.entityUuid));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), Translation_.value));
            }
            if (criteria.getEntityField() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityField(), Translation_.entityField));
            }
            if (criteria.getLanguageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLanguageId(),
                            root -> root.join(Translation_.language, JoinType.LEFT).get(Language_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
