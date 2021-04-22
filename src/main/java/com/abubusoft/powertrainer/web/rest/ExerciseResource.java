package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.ExerciseRepository;
import com.abubusoft.powertrainer.service.ExerciseQueryService;
import com.abubusoft.powertrainer.service.ExerciseService;
import com.abubusoft.powertrainer.service.criteria.ExerciseCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Exercise}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseResource.class);

    private static final String ENTITY_NAME = "exercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseService exerciseService;

    private final ExerciseRepository exerciseRepository;

    private final ExerciseQueryService exerciseQueryService;

    public ExerciseResource(
        ExerciseService exerciseService,
        ExerciseRepository exerciseRepository,
        ExerciseQueryService exerciseQueryService
    ) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
        this.exerciseQueryService = exerciseQueryService;
    }

    /**
     * {@code POST  /exercises} : Create a new exercise.
     *
     * @param exerciseDTO the exerciseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseDTO, or with status {@code 400 (Bad Request)} if the exercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercises")
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) throws URISyntaxException {
        log.debug("REST request to save Exercise : {}", exerciseDTO);
        if (exerciseDTO.getId() != null) {
            throw new BadRequestAlertException("A new exercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseDTO result = exerciseService.save(exerciseDTO);
        return ResponseEntity
            .created(new URI("/api/exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercises/:id} : Updates an existing exercise.
     *
     * @param id the id of the exerciseDTO to save.
     * @param exerciseDTO the exerciseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseDTO,
     * or with status {@code 400 (Bad Request)} if the exerciseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercises/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseDTO exerciseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Exercise : {}, {}", id, exerciseDTO);
        if (exerciseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseDTO result = exerciseService.save(exerciseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercises/:id} : Partial updates given fields of an existing exercise, field will ignore if it is null
     *
     * @param id the id of the exerciseDTO to save.
     * @param exerciseDTO the exerciseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseDTO,
     * or with status {@code 400 (Bad Request)} if the exerciseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExerciseDTO> partialUpdateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseDTO exerciseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exercise partially : {}, {}", id, exerciseDTO);
        if (exerciseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseDTO> result = exerciseService.partialUpdate(exerciseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exercises} : get all the exercises.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exercises in body.
     */
    @GetMapping("/exercises")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises(ExerciseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Exercises by criteria: {}", criteria);
        Page<ExerciseDTO> page = exerciseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercises/count} : count all the exercises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exercises/count")
    public ResponseEntity<Long> countExercises(ExerciseCriteria criteria) {
        log.debug("REST request to count Exercises by criteria: {}", criteria);
        return ResponseEntity.ok().body(exerciseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exercises/:id} : get the "id" exercise.
     *
     * @param id the id of the exerciseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercises/{id}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
        log.debug("REST request to get Exercise : {}", id);
        Optional<ExerciseDTO> exerciseDTO = exerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseDTO);
    }

    /**
     * {@code DELETE  /exercises/:id} : delete the "id" exercise.
     *
     * @param id the id of the exerciseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        log.debug("REST request to delete Exercise : {}", id);
        exerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
