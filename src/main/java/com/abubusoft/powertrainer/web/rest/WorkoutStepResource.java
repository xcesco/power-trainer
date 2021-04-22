package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.WorkoutStepQueryService;
import com.abubusoft.powertrainer.service.WorkoutStepService;
import com.abubusoft.powertrainer.service.criteria.WorkoutStepCriteria;
import com.abubusoft.powertrainer.service.dto.WorkoutStepDTO;
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
     * @param workoutStepDTO the workoutStepDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutStepDTO, or with status {@code 400 (Bad Request)} if the workoutStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-steps")
    public ResponseEntity<WorkoutStepDTO> createWorkoutStep(@Valid @RequestBody WorkoutStepDTO workoutStepDTO) throws URISyntaxException {
        log.debug("REST request to save WorkoutStep : {}", workoutStepDTO);
        if (workoutStepDTO.getId() != null) {
            throw new BadRequestAlertException("A new workoutStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutStepDTO result = workoutStepService.save(workoutStepDTO);
        return ResponseEntity
            .created(new URI("/api/workout-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-steps/:id} : Updates an existing workoutStep.
     *
     * @param id the id of the workoutStepDTO to save.
     * @param workoutStepDTO the workoutStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStepDTO,
     * or with status {@code 400 (Bad Request)} if the workoutStepDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-steps/{id}")
    public ResponseEntity<WorkoutStepDTO> updateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutStepDTO workoutStepDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutStep : {}, {}", id, workoutStepDTO);
        if (workoutStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutStepDTO result = workoutStepService.save(workoutStepDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutStepDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-steps/:id} : Partial updates given fields of an existing workoutStep, field will ignore if it is null
     *
     * @param id the id of the workoutStepDTO to save.
     * @param workoutStepDTO the workoutStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutStepDTO,
     * or with status {@code 400 (Bad Request)} if the workoutStepDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workoutStepDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-steps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutStepDTO> partialUpdateWorkoutStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutStepDTO workoutStepDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutStep partially : {}, {}", id, workoutStepDTO);
        if (workoutStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutStepDTO> result = workoutStepService.partialUpdate(workoutStepDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutStepDTO.getId().toString())
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
    public ResponseEntity<List<WorkoutStepDTO>> getAllWorkoutSteps(WorkoutStepCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WorkoutSteps by criteria: {}", criteria);
        Page<WorkoutStepDTO> page = workoutStepQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the workoutStepDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutStepDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-steps/{id}")
    public ResponseEntity<WorkoutStepDTO> getWorkoutStep(@PathVariable Long id) {
        log.debug("REST request to get WorkoutStep : {}", id);
        Optional<WorkoutStepDTO> workoutStepDTO = workoutStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutStepDTO);
    }

    /**
     * {@code DELETE  /workout-steps/:id} : delete the "id" workoutStep.
     *
     * @param id the id of the workoutStepDTO to delete.
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
