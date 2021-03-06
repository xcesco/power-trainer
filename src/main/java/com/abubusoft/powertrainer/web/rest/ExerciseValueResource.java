package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.ExerciseValueQueryService;
import com.abubusoft.powertrainer.service.ExerciseValueService;
import com.abubusoft.powertrainer.service.criteria.ExerciseValueCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.ExerciseValue}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseValueResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseValueResource.class);

    private static final String ENTITY_NAME = "exerciseValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseValueService exerciseValueService;

    private final ExerciseValueRepository exerciseValueRepository;

    private final ExerciseValueQueryService exerciseValueQueryService;

    public ExerciseValueResource(
        ExerciseValueService exerciseValueService,
        ExerciseValueRepository exerciseValueRepository,
        ExerciseValueQueryService exerciseValueQueryService
    ) {
        this.exerciseValueService = exerciseValueService;
        this.exerciseValueRepository = exerciseValueRepository;
        this.exerciseValueQueryService = exerciseValueQueryService;
    }

    /**
     * {@code POST  /exercise-values} : Create a new exerciseValue.
     *
     * @param exerciseValueDTO the exerciseValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseValueDTO, or with status {@code 400 (Bad Request)} if the exerciseValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-values")
    public ResponseEntity<ExerciseValueDTO> createExerciseValue(@Valid @RequestBody ExerciseValueDTO exerciseValueDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExerciseValue : {}", exerciseValueDTO);
        if (exerciseValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new exerciseValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseValueDTO result = exerciseValueService.save(exerciseValueDTO);
        return ResponseEntity
            .created(new URI("/api/exercise-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-values/:id} : Updates an existing exerciseValue.
     *
     * @param id the id of the exerciseValueDTO to save.
     * @param exerciseValueDTO the exerciseValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseValueDTO,
     * or with status {@code 400 (Bad Request)} if the exerciseValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-values/{id}")
    public ResponseEntity<ExerciseValueDTO> updateExerciseValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseValueDTO exerciseValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseValue : {}, {}", id, exerciseValueDTO);
        if (exerciseValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseValueDTO result = exerciseValueService.save(exerciseValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-values/:id} : Partial updates given fields of an existing exerciseValue, field will ignore if it is null
     *
     * @param id the id of the exerciseValueDTO to save.
     * @param exerciseValueDTO the exerciseValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseValueDTO,
     * or with status {@code 400 (Bad Request)} if the exerciseValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExerciseValueDTO> partialUpdateExerciseValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseValueDTO exerciseValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseValue partially : {}, {}", id, exerciseValueDTO);
        if (exerciseValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseValueDTO> result = exerciseValueService.partialUpdate(exerciseValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-values} : get all the exerciseValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseValues in body.
     */
    @GetMapping("/exercise-values")
    public ResponseEntity<List<ExerciseValueDTO>> getAllExerciseValues(ExerciseValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExerciseValues by criteria: {}", criteria);
        Page<ExerciseValueDTO> page = exerciseValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercise-values/count} : count all the exerciseValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exercise-values/count")
    public ResponseEntity<Long> countExerciseValues(ExerciseValueCriteria criteria) {
        log.debug("REST request to count ExerciseValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(exerciseValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exercise-values/:id} : get the "id" exerciseValue.
     *
     * @param id the id of the exerciseValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-values/{id}")
    public ResponseEntity<ExerciseValueDTO> getExerciseValue(@PathVariable Long id) {
        log.debug("REST request to get ExerciseValue : {}", id);
        Optional<ExerciseValueDTO> exerciseValueDTO = exerciseValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseValueDTO);
    }

    /**
     * {@code DELETE  /exercise-values/:id} : delete the "id" exerciseValue.
     *
     * @param id the id of the exerciseValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-values/{id}")
    public ResponseEntity<Void> deleteExerciseValue(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseValue : {}", id);
        exerciseValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
