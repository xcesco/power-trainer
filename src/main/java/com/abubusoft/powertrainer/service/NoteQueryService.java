package com.abubusoft.powertrainer.service;

import com.abubusoft.powertrainer.domain.*; // for static metamodels
import com.abubusoft.powertrainer.domain.Note;
import com.abubusoft.powertrainer.repository.NoteRepository;
import com.abubusoft.powertrainer.service.criteria.NoteCriteria;
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
 * Service for executing complex queries for {@link Note} entities in the database.
 * The main input is a {@link NoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Note} or a {@link Page} of {@link Note} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NoteQueryService extends QueryService<Note> {

    private final Logger log = LoggerFactory.getLogger(NoteQueryService.class);

    private final NoteRepository noteRepository;

    public NoteQueryService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Return a {@link List} of {@link Note} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Note> findByCriteria(NoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Note> specification = createSpecification(criteria);
        return noteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Note} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Note> findByCriteria(NoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Note> specification = createSpecification(criteria);
        return noteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Note> specification = createSpecification(criteria);
        return noteRepository.count(specification);
    }

    /**
     * Function to convert {@link NoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Note> createSpecification(NoteCriteria criteria) {
        Specification<Note> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Note_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Note_.uuid));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Note_.type));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Note_.url));
            }
            if (criteria.getExerciseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExerciseId(), root -> root.join(Note_.exercise, JoinType.LEFT).get(Exercise_.id))
                    );
            }
        }
        return specification;
    }
}
