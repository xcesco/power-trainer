package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import com.abubusoft.powertrainer.repository.WorkoutSheetExerciseRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetExerciseQueryService;
import com.abubusoft.powertrainer.service.WorkoutSheetExerciseService;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetExerciseCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.WorkoutSheetExercise}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutSheetExerciseResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetExerciseResource.class);

    private static final String ENTITY_NAME = "workoutSheetExercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutSheetExerciseService workoutSheetExerciseService;

    private final WorkoutSheetExerciseRepository workoutSheetExerciseRepository;

    private final WorkoutSheetExerciseQueryService workoutSheetExerciseQueryService;

    public WorkoutSheetExerciseResource(
        WorkoutSheetExerciseService workoutSheetExerciseService,
        WorkoutSheetExerciseRepository workoutSheetExerciseRepository,
        WorkoutSheetExerciseQueryService workoutSheetExerciseQueryService
    ) {
        this.workoutSheetExerciseService = workoutSheetExerciseService;
        this.workoutSheetExerciseRepository = workoutSheetExerciseRepository;
        this.workoutSheetExerciseQueryService = workoutSheetExerciseQueryService;
    }

    /**
     * {@code POST  /workout-sheet-exercises} : Create a new workoutSheetExercise.
     *
     * @param workoutSheetExercise the workoutSheetExercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutSheetExercise, or with status {@code 400 (Bad Request)} if the workoutSheetExercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-sheet-exercises")
    public ResponseEntity<WorkoutSheetExercise> createWorkoutSheetExercise(@Valid @RequestBody WorkoutSheetExercise workoutSheetExercise)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutSheetExercise : {}", workoutSheetExercise);
        if (workoutSheetExercise.getId() != null) {
            throw new BadRequestAlertException("A new workoutSheetExercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutSheetExercise result = workoutSheetExerciseService.save(workoutSheetExercise);
        return ResponseEntity
            .created(new URI("/api/workout-sheet-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-sheet-exercises/:id} : Updates an existing workoutSheetExercise.
     *
     * @param id the id of the workoutSheetExercise to save.
     * @param workoutSheetExercise the workoutSheetExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutSheetExercise,
     * or with status {@code 400 (Bad Request)} if the workoutSheetExercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutSheetExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-sheet-exercises/{id}")
    public ResponseEntity<WorkoutSheetExercise> updateWorkoutSheetExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutSheetExercise workoutSheetExercise
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutSheetExercise : {}, {}", id, workoutSheetExercise);
        if (workoutSheetExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutSheetExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutSheetExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutSheetExercise result = workoutSheetExerciseService.save(workoutSheetExercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutSheetExercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-sheet-exercises/:id} : Partial updates given fields of an existing workoutSheetExercise, field will ignore if it is null
     *
     * @param id the id of the workoutSheetExercise to save.
     * @param workoutSheetExercise the workoutSheetExercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutSheetExercise,
     * or with status {@code 400 (Bad Request)} if the workoutSheetExercise is not valid,
     * or with status {@code 404 (Not Found)} if the workoutSheetExercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutSheetExercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-sheet-exercises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutSheetExercise> partialUpdateWorkoutSheetExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutSheetExercise workoutSheetExercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutSheetExercise partially : {}, {}", id, workoutSheetExercise);
        if (workoutSheetExercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutSheetExercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutSheetExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutSheetExercise> result = workoutSheetExerciseService.partialUpdate(workoutSheetExercise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutSheetExercise.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-sheet-exercises} : get all the workoutSheetExercises.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutSheetExercises in body.
     */
    @GetMapping("/workout-sheet-exercises")
    public ResponseEntity<List<WorkoutSheetExercise>> getAllWorkoutSheetExercises(
        WorkoutSheetExerciseCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkoutSheetExercises by criteria: {}", criteria);
        Page<WorkoutSheetExercise> page = workoutSheetExerciseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workout-sheet-exercises/count} : count all the workoutSheetExercises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/workout-sheet-exercises/count")
    public ResponseEntity<Long> countWorkoutSheetExercises(WorkoutSheetExerciseCriteria criteria) {
        log.debug("REST request to count WorkoutSheetExercises by criteria: {}", criteria);
        return ResponseEntity.ok().body(workoutSheetExerciseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workout-sheet-exercises/:id} : get the "id" workoutSheetExercise.
     *
     * @param id the id of the workoutSheetExercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutSheetExercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-sheet-exercises/{id}")
    public ResponseEntity<WorkoutSheetExercise> getWorkoutSheetExercise(@PathVariable Long id) {
        log.debug("REST request to get WorkoutSheetExercise : {}", id);
        Optional<WorkoutSheetExercise> workoutSheetExercise = workoutSheetExerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutSheetExercise);
    }

    /**
     * {@code DELETE  /workout-sheet-exercises/:id} : delete the "id" workoutSheetExercise.
     *
     * @param id the id of the workoutSheetExercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-sheet-exercises/{id}")
    public ResponseEntity<Void> deleteWorkoutSheetExercise(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutSheetExercise : {}", id);
        workoutSheetExerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
