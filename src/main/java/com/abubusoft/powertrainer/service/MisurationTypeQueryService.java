package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.MisurationType;
import com.abubusoft.powertrainer.repository.MisurationTypeRepository;
import com.abubusoft.powertrainer.service.criteria.MisurationTypeCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationTypeMapper;
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
 * Service for executing complex queries for {@link MisurationType} entities in the database.
 * The main input is a {@link MisurationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MisurationTypeDTO} or a {@link Page} of {@link MisurationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MisurationTypeQueryService extends QueryService<MisurationType> {

    private final Logger log = LoggerFactory.getLogger(MisurationTypeQueryService.class);

    private final MisurationTypeRepository misurationTypeRepository;

    private final MisurationTypeMapper misurationTypeMapper;

    public MisurationTypeQueryService(MisurationTypeRepository misurationTypeRepository, MisurationTypeMapper misurationTypeMapper) {
        this.misurationTypeRepository = misurationTypeRepository;
        this.misurationTypeMapper = misurationTypeMapper;
    }

    /**
     * Return a {@link List} of {@link MisurationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MisurationTypeDTO> findByCriteria(MisurationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MisurationType> specification = createSpecification(criteria);
        return misurationTypeMapper.toDto(misurationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MisurationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MisurationTypeDTO> findByCriteria(MisurationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MisurationType> specification = createSpecification(criteria);
        return misurationTypeRepository.findAll(specification, page).map(misurationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MisurationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MisurationType> specification = createSpecification(criteria);
        return misurationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link MisurationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MisurationType> createSpecification(MisurationTypeCriteria criteria) {
        Specification<MisurationType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MisurationType_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), MisurationType_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MisurationType_.name));
            }
            if (criteria.getMisurationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMisurationId(),
                            root -> root.join(MisurationType_.misurations, JoinType.LEFT).get(Misuration_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
