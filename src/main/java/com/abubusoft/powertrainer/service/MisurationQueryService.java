package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.repository.MisurationRepository;
import com.abubusoft.powertrainer.service.criteria.MisurationCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationMapper;
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
 * Service for executing complex queries for {@link Misuration} entities in the database.
 * The main input is a {@link MisurationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MisurationDTO} or a {@link Page} of {@link MisurationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MisurationQueryService extends QueryService<Misuration> {

    private final Logger log = LoggerFactory.getLogger(MisurationQueryService.class);

    private final MisurationRepository misurationRepository;

    private final MisurationMapper misurationMapper;

    public MisurationQueryService(MisurationRepository misurationRepository, MisurationMapper misurationMapper) {
        this.misurationRepository = misurationRepository;
        this.misurationMapper = misurationMapper;
    }

    /**
     * Return a {@link List} of {@link MisurationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MisurationDTO> findByCriteria(MisurationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Misuration> specification = createSpecification(criteria);
        return misurationMapper.toDto(misurationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MisurationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MisurationDTO> findByCriteria(MisurationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Misuration> specification = createSpecification(criteria);
        return misurationRepository.findAll(specification, page).map(misurationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MisurationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Misuration> specification = createSpecification(criteria);
        return misurationRepository.count(specification);
    }

    /**
     * Function to convert {@link MisurationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Misuration> createSpecification(MisurationCriteria criteria) {
        Specification<Misuration> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Misuration_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Misuration_.uuid));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Misuration_.date));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), Misuration_.value));
            }
            if (criteria.getCalendarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCalendarId(),
                            root -> root.join(Misuration_.calendar, JoinType.LEFT).get(Calendar_.id)
                        )
                    );
            }
            if (criteria.getMisurationTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMisurationTypeId(),
                            root -> root.join(Misuration_.misurationType, JoinType.LEFT).get(MisurationType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
