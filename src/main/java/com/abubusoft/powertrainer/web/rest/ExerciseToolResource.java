package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.repository.ExerciseToolRepository;
import com.abubusoft.powertrainer.service.ExerciseToolQueryService;
import com.abubusoft.powertrainer.service.ExerciseToolService;
import com.abubusoft.powertrainer.service.criteria.ExerciseToolCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.ExerciseTool}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseToolResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseToolResource.class);

    private static final String ENTITY_NAME = "exerciseTool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseToolService exerciseToolService;

    private final ExerciseToolRepository exerciseToolRepository;

    private final ExerciseToolQueryService exerciseToolQueryService;

    public ExerciseToolResource(
        ExerciseToolService exerciseToolService,
        ExerciseToolRepository exerciseToolRepository,
        ExerciseToolQueryService exerciseToolQueryService
    ) {
        this.exerciseToolService = exerciseToolService;
        this.exerciseToolRepository = exerciseToolRepository;
        this.exerciseToolQueryService = exerciseToolQueryService;
    }

    /**
     * {@code POST  /exercise-tools} : Create a new exerciseTool.
     *
     * @param exerciseTool the exerciseTool to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseTool, or with status {@code 400 (Bad Request)} if the exerciseTool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-tools")
    public ResponseEntity<ExerciseTool> createExerciseTool(@Valid @RequestBody ExerciseTool exerciseTool) throws URISyntaxException {
        log.debug("REST request to save ExerciseTool : {}", exerciseTool);
        if (exerciseTool.getId() != null) {
            throw new BadRequestAlertException("A new exerciseTool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseTool result = exerciseToolService.save(exerciseTool);
        return ResponseEntity
            .created(new URI("/api/exercise-tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-tools/:id} : Updates an existing exerciseTool.
     *
     * @param id the id of the exerciseTool to save.
     * @param exerciseTool the exerciseTool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseTool,
     * or with status {@code 400 (Bad Request)} if the exerciseTool is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseTool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-tools/{id}")
    public ResponseEntity<ExerciseTool> updateExerciseTool(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseTool exerciseTool
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseTool : {}, {}", id, exerciseTool);
        if (exerciseTool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseTool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseToolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseTool result = exerciseToolService.save(exerciseTool);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseTool.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-tools/:id} : Partial updates given fields of an existing exerciseTool, field will ignore if it is null
     *
     * @param id the id of the exerciseTool to save.
     * @param exerciseTool the exerciseTool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseTool,
     * or with status {@code 400 (Bad Request)} if the exerciseTool is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseTool is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseTool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-tools/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExerciseTool> partialUpdateExerciseTool(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseTool exerciseTool
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseTool partially : {}, {}", id, exerciseTool);
        if (exerciseTool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseTool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseToolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseTool> result = exerciseToolService.partialUpdate(exerciseTool);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseTool.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-tools} : get all the exerciseTools.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseTools in body.
     */
    @GetMapping("/exercise-tools")
    public ResponseEntity<List<ExerciseTool>> getAllExerciseTools(ExerciseToolCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExerciseTools by criteria: {}", criteria);
        Page<ExerciseTool> page = exerciseToolQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercise-tools/count} : count all the exerciseTools.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exercise-tools/count")
    public ResponseEntity<Long> countExerciseTools(ExerciseToolCriteria criteria) {
        log.debug("REST request to count ExerciseTools by criteria: {}", criteria);
        return ResponseEntity.ok().body(exerciseToolQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exercise-tools/:id} : get the "id" exerciseTool.
     *
     * @param id the id of the exerciseTool to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseTool, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-tools/{id}")
    public ResponseEntity<ExerciseTool> getExerciseTool(@PathVariable Long id) {
        log.debug("REST request to get ExerciseTool : {}", id);
        Optional<ExerciseTool> exerciseTool = exerciseToolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseTool);
    }

    /**
     * {@code DELETE  /exercise-tools/:id} : delete the "id" exerciseTool.
     *
     * @param id the id of the exerciseTool to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-tools/{id}")
    public ResponseEntity<Void> deleteExerciseTool(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseTool : {}", id);
        exerciseToolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
