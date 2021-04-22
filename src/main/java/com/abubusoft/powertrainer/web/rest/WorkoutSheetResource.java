package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.WorkoutSheetRepository;
import com.abubusoft.powertrainer.service.WorkoutSheetQueryService;
import com.abubusoft.powertrainer.service.WorkoutSheetService;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetCriteria;
import com.abubusoft.powertrainer.service.dto.WorkoutSheetDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.WorkoutSheet}.
 */
@RestController
@RequestMapping("/api")
public class WorkoutSheetResource {

    private final Logger log = LoggerFactory.getLogger(WorkoutSheetResource.class);

    private static final String ENTITY_NAME = "workoutSheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkoutSheetService workoutSheetService;

    private final WorkoutSheetRepository workoutSheetRepository;

    private final WorkoutSheetQueryService workoutSheetQueryService;

    public WorkoutSheetResource(
        WorkoutSheetService workoutSheetService,
        WorkoutSheetRepository workoutSheetRepository,
        WorkoutSheetQueryService workoutSheetQueryService
    ) {
        this.workoutSheetService = workoutSheetService;
        this.workoutSheetRepository = workoutSheetRepository;
        this.workoutSheetQueryService = workoutSheetQueryService;
    }

    /**
     * {@code POST  /workout-sheets} : Create a new workoutSheet.
     *
     * @param workoutSheetDTO the workoutSheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workoutSheetDTO, or with status {@code 400 (Bad Request)} if the workoutSheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workout-sheets")
    public ResponseEntity<WorkoutSheetDTO> createWorkoutSheet(@Valid @RequestBody WorkoutSheetDTO workoutSheetDTO)
        throws URISyntaxException {
        log.debug("REST request to save WorkoutSheet : {}", workoutSheetDTO);
        if (workoutSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new workoutSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkoutSheetDTO result = workoutSheetService.save(workoutSheetDTO);
        return ResponseEntity
            .created(new URI("/api/workout-sheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workout-sheets/:id} : Updates an existing workoutSheet.
     *
     * @param id the id of the workoutSheetDTO to save.
     * @param workoutSheetDTO the workoutSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutSheetDTO,
     * or with status {@code 400 (Bad Request)} if the workoutSheetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workoutSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workout-sheets/{id}")
    public ResponseEntity<WorkoutSheetDTO> updateWorkoutSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkoutSheetDTO workoutSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkoutSheet : {}, {}", id, workoutSheetDTO);
        if (workoutSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkoutSheetDTO result = workoutSheetService.save(workoutSheetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutSheetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workout-sheets/:id} : Partial updates given fields of an existing workoutSheet, field will ignore if it is null
     *
     * @param id the id of the workoutSheetDTO to save.
     * @param workoutSheetDTO the workoutSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workoutSheetDTO,
     * or with status {@code 400 (Bad Request)} if the workoutSheetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workoutSheetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workoutSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workout-sheets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkoutSheetDTO> partialUpdateWorkoutSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkoutSheetDTO workoutSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkoutSheet partially : {}, {}", id, workoutSheetDTO);
        if (workoutSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workoutSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workoutSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkoutSheetDTO> result = workoutSheetService.partialUpdate(workoutSheetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workoutSheetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workout-sheets} : get all the workoutSheets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workoutSheets in body.
     */
    @GetMapping("/workout-sheets")
    public ResponseEntity<List<WorkoutSheetDTO>> getAllWorkoutSheets(WorkoutSheetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WorkoutSheets by criteria: {}", criteria);
        Page<WorkoutSheetDTO> page = workoutSheetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workout-sheets/count} : count all the workoutSheets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/workout-sheets/count")
    public ResponseEntity<Long> countWorkoutSheets(WorkoutSheetCriteria criteria) {
        log.debug("REST request to count WorkoutSheets by criteria: {}", criteria);
        return ResponseEntity.ok().body(workoutSheetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workout-sheets/:id} : get the "id" workoutSheet.
     *
     * @param id the id of the workoutSheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workoutSheetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workout-sheets/{id}")
    public ResponseEntity<WorkoutSheetDTO> getWorkoutSheet(@PathVariable Long id) {
        log.debug("REST request to get WorkoutSheet : {}", id);
        Optional<WorkoutSheetDTO> workoutSheetDTO = workoutSheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workoutSheetDTO);
    }

    /**
     * {@code DELETE  /workout-sheets/:id} : delete the "id" workoutSheet.
     *
     * @param id the id of the workoutSheetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workout-sheets/{id}")
    public ResponseEntity<Void> deleteWorkoutSheet(@PathVariable Long id) {
        log.debug("REST request to delete WorkoutSheet : {}", id);
        workoutSheetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
