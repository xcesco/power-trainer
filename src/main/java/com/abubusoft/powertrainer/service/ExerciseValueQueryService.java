package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseValueCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseValueMapper;
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
 * Service for executing complex queries for {@link ExerciseValue} entities in the database.
 * The main input is a {@link ExerciseValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseValueDTO} or a {@link Page} of {@link ExerciseValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseValueQueryService extends QueryService<ExerciseValue> {

    private final Logger log = LoggerFactory.getLogger(ExerciseValueQueryService.class);

    private final ExerciseValueRepository exerciseValueRepository;

    private final ExerciseValueMapper exerciseValueMapper;

    public ExerciseValueQueryService(ExerciseValueRepository exerciseValueRepository, ExerciseValueMapper exerciseValueMapper) {
        this.exerciseValueRepository = exerciseValueRepository;
        this.exerciseValueMapper = exerciseValueMapper;
    }

    /**
     * Return a {@link List} of {@link ExerciseValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseValueDTO> findByCriteria(ExerciseValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueMapper.toDto(exerciseValueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExerciseValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseValueDTO> findByCriteria(ExerciseValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueRepository.findAll(specification, page).map(exerciseValueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExerciseValue> specification = createSpecification(criteria);
        return exerciseValueRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExerciseValue> createSpecification(ExerciseValueCriteria criteria) {
        Specification<ExerciseValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExerciseValue_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), ExerciseValue_.uuid));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ExerciseValue_.date));
            }
            if (criteria.getExerciseUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseUuid(), ExerciseValue_.exerciseUuid));
            }
            if (criteria.getExerciseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExerciseName(), ExerciseValue_.exerciseName));
            }
            if (criteria.getExerciseValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExerciseValue(), ExerciseValue_.exerciseValue));
            }
            if (criteria.getExerciseValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseValueType(), ExerciseValue_.exerciseValueType));
            }
            if (criteria.getCalendarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCalendarId(),
                            root -> root.join(ExerciseValue_.calendar, JoinType.LEFT).get(Calendar_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
