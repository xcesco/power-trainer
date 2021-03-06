package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.WorkoutRepository;
import com.abubusoft.powertrainer.service.WorkoutQueryService;
import com.abubusoft.powertrainer.service.WorkoutService;
import com.abubusoft.powertrainer.service.criteria.WorkoutCriteria;
import com.abubusoft.powertrainer.service.dto.WorkoutDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Workout}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutResource.class);

    private static final String ENTITY_NAME = "workout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutService workoutService;

    private final WorkoutRepository workoutRepository;

    private final WorkoutQueryService workoutQueryService;

    public WorkoutResource(WorkoutService workoutService, WorkoutRepository workoutRepository, WorkoutQueryService workoutQueryService) {
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
        this.workoutQueryService = workoutQueryService;
    }

    /**
     * {@code POST  /workouts} : Create a new workout.
     *
     * @param workoutDTO the workoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutDTO, or with status {@code 400 (Bad Request)} if the workout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workouts")
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) throws URISyntaxException {
        log.debug("REST request to save Workout : {}", workoutDTO);
        if (workoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new workout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutDTO result = workoutService.save(workoutDTO);
        return ResponseEntity
            .created(new URI("/api/workouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workouts/:id} : Updates an existing workout.
     *
     * @param id the id of the workoutDTO to save.
     * @param workoutDTO the workoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutDTO,
     * or with status {@code 400 (Bad Request)} if the workoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workouts/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Workout : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutDTO result = workoutService.save(workoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workouts/:id} : Partial updates given fields of an existing workout, field will ignore if it is null
     *
     * @param id the id of the workoutDTO to save.
     * @param workoutDTO the workoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutDTO,
     * or with status {@code 400 (Bad Request)} if the workoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workouts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutDTO> partialUpdateWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutDTO workoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Workout partially : {}, {}", id, workoutDTO);
        if (workoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutDTO> result = workoutService.partialUpdate(workoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workouts} : get all the workouts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workouts in body.
     */
    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts(WorkoutCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Workouts by criteria: {}", criteria);
        Page<WorkoutDTO> page = workoutQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workouts/count} : count all the workouts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/workouts/count")
    public ResponseEntity<Long> countWorkouts(WorkoutCriteria criteria) {
        log.debug("REST request to count Workouts by criteria: {}", criteria);
        return ResponseEntity.ok().body(workoutQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workouts/:id} : get the "id" workout.
     *
     * @param id the id of the workoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workouts/{id}")
    public ResponseEntity<WorkoutDTO> getWorkout(@PathVariable Long id) {
        log.debug("REST request to get Workout : {}", id);
        Optional<WorkoutDTO> workoutDTO = workoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutDTO);
    }

    /**
     * {@code DELETE  /workouts/:id} : delete the "id" workout.
     *
     * @param id the id of the workoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        log.debug("REST request to delete Workout : {}", id);
        workoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
