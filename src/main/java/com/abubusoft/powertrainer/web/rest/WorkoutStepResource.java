package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.WorkoutStepQueryService;
import com.abubusoft.powertrainer.service.WorkoutStepService;
import com.abubusoft.powertrainer.service.criteria.WorkoutStepCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.WorkoutStep}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutStepResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutStepResource.class);

    private static final String ENTITY_NAME = "workoutStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutStepService workoutStepService;

    private final WorkoutStepRepository workoutStepRepository;

    private final WorkoutStepQueryService workoutStepQueryService;

    public WorkoutStepResource(
        WorkoutStepService workoutStepService,
        WorkoutStepRepository workoutStepRepository,
        WorkoutStepQueryService workoutStepQueryService
    ) {
        this.workoutStepService = workoutStepService;
        this.workoutStepRepository = workoutStepRepository;
        this.workoutStepQueryService = workoutStepQueryService;
    }

    /**
     * {@code POST  /workout-steps} : Create a new workoutStep.
     *
     * @param workoutStep the workoutStep to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutStep, or with status {@code 400 (Bad Request)} if the workoutStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-steps")
    public ResponseEntity<WorkoutStep> createWorkoutStep(@Valid @RequestBody WorkoutStep workoutStep) throws URISyntaxException {
        log.debug("REST request to save WorkoutStep : {}", workoutStep);
        if (workoutStep.getId() != null) {
            throw new BadRequestAlertException("A new workoutStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutStep result = workoutStepService.save(workoutStep);
        return ResponseEntity
            .created(new URI("/api/workout-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-steps/:id} : Updates an existing workoutStep.
     *
     * @param id the id of the workoutStep to save.
     * @param workoutStep the workoutStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStep,
     * or with status {@code 400 (Bad Request)} if the workoutStep is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-steps/{id}")
    public ResponseEntity<WorkoutStep> updateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutStep workoutStep
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutStep : {}, {}", id, workoutStep);
        if (workoutStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutStep result = workoutStepService.save(workoutStep);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutStep.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-steps/:id} : Partial updates given fields of an existing workoutStep, field will ignore if it is null
     *
     * @param id the id of the workoutStep to save.
     * @param workoutStep the workoutStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStep,
     * or with status {@code 400 (Bad Request)} if the workoutStep is not valid,
     * or with status {@code 404 (Not Found)} if the workoutStep is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-steps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutStep> partialUpdateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutStep workoutStep
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutStep partially : {}, {}", id, workoutStep);
        if (workoutStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStep.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutStep> result = workoutStepService.partialUpdate(workoutStep);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutStep.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-steps} : get all the workoutSteps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutSteps in body.
     */
    @GetMapping("/workout-steps")
    public ResponseEntity<List<WorkoutStep>> getAllWorkoutSteps(WorkoutStepCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WorkoutSteps by criteria: {}", criteria);
        Page<WorkoutStep> page = workoutStepQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workout-steps/count} : count all the workoutSteps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/workout-steps/count")
    public ResponseEntity<Long> countWorkoutSteps(WorkoutStepCriteria criteria) {
        log.debug("REST request to count WorkoutSteps by criteria: {}", criteria);
        return ResponseEntity.ok().body(workoutStepQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workout-steps/:id} : get the "id" workoutStep.
     *
     * @param id the id of the workoutStep to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutStep, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-steps/{id}")
    public ResponseEntity<WorkoutStep> getWorkoutStep(@PathVariable Long id) {
        log.debug("REST request to get WorkoutStep : {}", id);
        Optional<WorkoutStep> workoutStep = workoutStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutStep);
    }

    /**
     * {@code DELETE  /workout-steps/:id} : delete the "id" workoutStep.
     *
     * @param id the id of the workoutStep to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-steps/{id}")
    public ResponseEntity<Void> deleteWorkoutStep(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutStep : {}", id);
        workoutStepService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
