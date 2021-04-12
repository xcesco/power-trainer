package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.repository.CalendarRepository;
import com.abubusoft.powertrainer.service.criteria.CalendarCriteria;
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
 * Service for executing complex queries for {@link Calendar} entities in the database.
 * The main input is a {@link CalendarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Calendar} or a {@link Page} of {@link Calendar} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CalendarQueryService extends QueryService<Calendar> {

    private final Logger log = LoggerFactory.getLogger(CalendarQueryService.class);

    private final CalendarRepository calendarRepository;

    public CalendarQueryService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    /**
     * Return a {@link List} of {@link Calendar} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Calendar> findByCriteria(CalendarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Calendar} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Calendar> findByCriteria(CalendarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CalendarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Calendar> specification = createSpecification(criteria);
        return calendarRepository.count(specification);
    }

    /**
     * Function to convert {@link CalendarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Calendar> createSpecification(CalendarCriteria criteria) {
        Specification<Calendar> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Calendar_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Calendar_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Calendar_.name));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Calendar_.owner));
            }
            if (criteria.getExerciseValueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExerciseValueId(),
                            root -> root.join(Calendar_.exerciseValues, JoinType.LEFT).get(ExerciseValue_.id)
                        )
                    );
            }
            if (criteria.getMisurationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMisurationId(),
                            root -> root.join(Calendar_.misurations, JoinType.LEFT).get(Misuration_.id)
                        )
                    );
            }
            if (criteria.getWorkoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWorkoutId(), root -> root.join(Calendar_.workouts, JoinType.LEFT).get(Workout_.id))
                    );
            }
        }
        return specification;
    }
}
