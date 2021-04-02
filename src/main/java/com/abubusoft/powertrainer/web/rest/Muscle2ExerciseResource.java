package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import com.abubusoft.powertrainer.repository.Muscle2ExerciseRepository;
import com.abubusoft.powertrainer.service.Muscle2ExerciseQueryService;
import com.abubusoft.powertrainer.service.Muscle2ExerciseService;
import com.abubusoft.powertrainer.service.criteria.Muscle2ExerciseCriteria;
import com.abubusoft.powertrainer.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Muscle2Exercise}.
 */
@RestController
@RequestMapping("/api")
public class Muscle2ExerciseResource {

    private final Logger log = LoggerFactory.getLogger(Muscle2ExerciseResource.class);

    private static final String ENTITY_NAME = "muscle2Exercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Muscle2ExerciseService muscle2ExerciseService;

    private final Muscle2ExerciseRepository muscle2ExerciseRepository;

    private final Muscle2ExerciseQueryService muscle2ExerciseQueryService;

    public Muscle2ExerciseResource(
        Muscle2ExerciseService muscle2ExerciseService,
        Muscle2ExerciseRepository muscle2ExerciseRepository,
        Muscle2ExerciseQueryService muscle2ExerciseQueryService
    ) {
        this.muscle2ExerciseService = muscle2ExerciseService;
        this.muscle2ExerciseRepository = muscle2ExerciseRepository;
        this.muscle2ExerciseQueryService = muscle2ExerciseQueryService;
    }

    /**
     * {@code POST  /muscle-2-exercises} : Create a new muscle2Exercise.
     *
     * @param muscle2Exercise the muscle2Exercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new muscle2Exercise, or with status {@code 400 (Bad Request)} if the muscle2Exercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/muscle-2-exercises")
    public ResponseEntity<Muscle2Exercise> createMuscle2Exercise(@RequestBody Muscle2Exercise muscle2Exercise) throws URISyntaxException {
        log.debug("REST request to save Muscle2Exercise : {}", muscle2Exercise);
        if (muscle2Exercise.getId() != null) {
            throw new BadRequestAlertException("A new muscle2Exercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Muscle2Exercise result = muscle2ExerciseService.save(muscle2Exercise);
        return ResponseEntity
            .created(new URI("/api/muscle-2-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /muscle-2-exercises/:id} : Updates an existing muscle2Exercise.
     *
     * @param id the id of the muscle2Exercise to save.
     * @param muscle2Exercise the muscle2Exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated muscle2Exercise,
     * or with status {@code 400 (Bad Request)} if the muscle2Exercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the muscle2Exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/muscle-2-exercises/{id}")
    public ResponseEntity<Muscle2Exercise> updateMuscle2Exercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Muscle2Exercise muscle2Exercise
    ) throws URISyntaxException {
        log.debug("REST request to update Muscle2Exercise : {}, {}", id, muscle2Exercise);
        if (muscle2Exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, muscle2Exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!muscle2ExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Muscle2Exercise result = muscle2ExerciseService.save(muscle2Exercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, muscle2Exercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /muscle-2-exercises/:id} : Partial updates given fields of an existing muscle2Exercise, field will ignore if it is null
     *
     * @param id the id of the muscle2Exercise to save.
     * @param muscle2Exercise the muscle2Exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated muscle2Exercise,
     * or with status {@code 400 (Bad Request)} if the muscle2Exercise is not valid,
     * or with status {@code 404 (Not Found)} if the muscle2Exercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the muscle2Exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/muscle-2-exercises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Muscle2Exercise> partialUpdateMuscle2Exercise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Muscle2Exercise muscle2Exercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Muscle2Exercise partially : {}, {}", id, muscle2Exercise);
        if (muscle2Exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, muscle2Exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!muscle2ExerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Muscle2Exercise> result = muscle2ExerciseService.partialUpdate(muscle2Exercise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, muscle2Exercise.getId().toString())
        );
    }

    /**
     * {@code GET  /muscle-2-exercises} : get all the muscle2Exercises.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of muscle2Exercises in body.
     */
    @GetMapping("/muscle-2-exercises")
    public ResponseEntity<List<Muscle2Exercise>> getAllMuscle2Exercises(Muscle2ExerciseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Muscle2Exercises by criteria: {}", criteria);
        Page<Muscle2Exercise> page = muscle2ExerciseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /muscle-2-exercises/count} : count all the muscle2Exercises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/muscle-2-exercises/count")
    public ResponseEntity<Long> countMuscle2Exercises(Muscle2ExerciseCriteria criteria) {
        log.debug("REST request to count Muscle2Exercises by criteria: {}", criteria);
        return ResponseEntity.ok().body(muscle2ExerciseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /muscle-2-exercises/:id} : get the "id" muscle2Exercise.
     *
     * @param id the id of the muscle2Exercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the muscle2Exercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/muscle-2-exercises/{id}")
    public ResponseEntity<Muscle2Exercise> getMuscle2Exercise(@PathVariable Long id) {
        log.debug("REST request to get Muscle2Exercise : {}", id);
        Optional<Muscle2Exercise> muscle2Exercise = muscle2ExerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(muscle2Exercise);
    }

    /**
     * {@code DELETE  /muscle-2-exercises/:id} : delete the "id" muscle2Exercise.
     *
     * @param id the id of the muscle2Exercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/muscle-2-exercises/{id}")
    public ResponseEntity<Void> deleteMuscle2Exercise(@PathVariable Long id) {
        log.debug("REST request to delete Muscle2Exercise : {}", id);
        muscle2ExerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
