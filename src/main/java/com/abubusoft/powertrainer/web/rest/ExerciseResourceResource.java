package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.repository.ExerciseResourceRepository;
import com.abubusoft.powertrainer.service.ExerciseResourceQueryService;
import com.abubusoft.powertrainer.service.ExerciseResourceService;
import com.abubusoft.powertrainer.service.criteria.ExerciseResourceCriteria;
import com.abubusoft.powertrainer.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.ExerciseResource}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseResourceResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseResourceResource.class);

    private static final String ENTITY_NAME = "exerciseResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseResourceService exerciseResourceService;

    private final ExerciseResourceRepository exerciseResourceRepository;

    private final ExerciseResourceQueryService exerciseResourceQueryService;

    public ExerciseResourceResource(
        ExerciseResourceService exerciseResourceService,
        ExerciseResourceRepository exerciseResourceRepository,
        ExerciseResourceQueryService exerciseResourceQueryService
    ) {
        this.exerciseResourceService = exerciseResourceService;
        this.exerciseResourceRepository = exerciseResourceRepository;
        this.exerciseResourceQueryService = exerciseResourceQueryService;
    }

    /**
     * {@code POST  /exercise-resources} : Create a new exerciseResource.
     *
     * @param exerciseResource the exerciseResource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseResource, or with status {@code 400 (Bad Request)} if the exerciseResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-resources")
    public ResponseEntity<ExerciseResource> createExerciseResource(@Valid @RequestBody ExerciseResource exerciseResource)
        throws URISyntaxException {
        log.debug("REST request to save ExerciseResource : {}", exerciseResource);
        if (exerciseResource.getId() != null) {
            throw new BadRequestAlertException("A new exerciseResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseResource result = exerciseResourceService.save(exerciseResource);
        return ResponseEntity
            .created(new URI("/api/exercise-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-resources/:id} : Updates an existing exerciseResource.
     *
     * @param id the id of the exerciseResource to save.
     * @param exerciseResource the exerciseResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseResource,
     * or with status {@code 400 (Bad Request)} if the exerciseResource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-resources/{id}")
    public ResponseEntity<ExerciseResource> updateExerciseResource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseResource exerciseResource
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseResource : {}, {}", id, exerciseResource);
        if (exerciseResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseResource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseResource result = exerciseResourceService.save(exerciseResource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseResource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-resources/:id} : Partial updates given fields of an existing exerciseResource, field will ignore if it is null
     *
     * @param id the id of the exerciseResource to save.
     * @param exerciseResource the exerciseResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseResource,
     * or with status {@code 400 (Bad Request)} if the exerciseResource is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseResource is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-resources/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExerciseResource> partialUpdateExerciseResource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseResource exerciseResource
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseResource partially : {}, {}", id, exerciseResource);
        if (exerciseResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseResource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseResourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseResource> result = exerciseResourceService.partialUpdate(exerciseResource);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseResource.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-resources} : get all the exerciseResources.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseResources in body.
     */
    @GetMapping("/exercise-resources")
    public ResponseEntity<List<ExerciseResource>> getAllExerciseResources(ExerciseResourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExerciseResources by criteria: {}", criteria);
        Page<ExerciseResource> page = exerciseResourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercise-resources/count} : count all the exerciseResources.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exercise-resources/count")
    public ResponseEntity<Long> countExerciseResources(ExerciseResourceCriteria criteria) {
        log.debug("REST request to count ExerciseResources by criteria: {}", criteria);
        return ResponseEntity.ok().body(exerciseResourceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exercise-resources/:id} : get the "id" exerciseResource.
     *
     * @param id the id of the exerciseResource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseResource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-resources/{id}")
    public ResponseEntity<ExerciseResource> getExerciseResource(@PathVariable Long id) {
        log.debug("REST request to get ExerciseResource : {}", id);
        Optional<ExerciseResource> exerciseResource = exerciseResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseResource);
    }

    /**
     * {@code DELETE  /exercise-resources/:id} : delete the "id" exerciseResource.
     *
     * @param id the id of the exerciseResource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-resources/{id}")
    public ResponseEntity<Void> deleteExerciseResource(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseResource : {}", id);
        exerciseResourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
