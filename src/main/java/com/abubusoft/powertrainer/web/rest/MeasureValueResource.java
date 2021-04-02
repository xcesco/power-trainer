package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.MeasureValue;
import com.abubusoft.powertrainer.repository.MeasureValueRepository;
import com.abubusoft.powertrainer.service.MeasureValueQueryService;
import com.abubusoft.powertrainer.service.MeasureValueService;
import com.abubusoft.powertrainer.service.criteria.MeasureValueCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.MeasureValue}.
 */
@RestController
@RequestMapping("/api")
public class MeasureValueResource {

    private final Logger log = LoggerFactory.getLogger(MeasureValueResource.class);

    private static final String ENTITY_NAME = "measureValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasureValueService measureValueService;

    private final MeasureValueRepository measureValueRepository;

    private final MeasureValueQueryService measureValueQueryService;

    public MeasureValueResource(
        MeasureValueService measureValueService,
        MeasureValueRepository measureValueRepository,
        MeasureValueQueryService measureValueQueryService
    ) {
        this.measureValueService = measureValueService;
        this.measureValueRepository = measureValueRepository;
        this.measureValueQueryService = measureValueQueryService;
    }

    /**
     * {@code POST  /measure-values} : Create a new measureValue.
     *
     * @param measureValue the measureValue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measureValue, or with status {@code 400 (Bad Request)} if the measureValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measure-values")
    public ResponseEntity<MeasureValue> createMeasureValue(@Valid @RequestBody MeasureValue measureValue) throws URISyntaxException {
        log.debug("REST request to save MeasureValue : {}", measureValue);
        if (measureValue.getId() != null) {
            throw new BadRequestAlertException("A new measureValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeasureValue result = measureValueService.save(measureValue);
        return ResponseEntity
            .created(new URI("/api/measure-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measure-values/:id} : Updates an existing measureValue.
     *
     * @param id the id of the measureValue to save.
     * @param measureValue the measureValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureValue,
     * or with status {@code 400 (Bad Request)} if the measureValue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measureValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measure-values/{id}")
    public ResponseEntity<MeasureValue> updateMeasureValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MeasureValue measureValue
    ) throws URISyntaxException {
        log.debug("REST request to update MeasureValue : {}, {}", id, measureValue);
        if (measureValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeasureValue result = measureValueService.save(measureValue);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureValue.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /measure-values/:id} : Partial updates given fields of an existing measureValue, field will ignore if it is null
     *
     * @param id the id of the measureValue to save.
     * @param measureValue the measureValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureValue,
     * or with status {@code 400 (Bad Request)} if the measureValue is not valid,
     * or with status {@code 404 (Not Found)} if the measureValue is not found,
     * or with status {@code 500 (Internal Server Error)} if the measureValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/measure-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MeasureValue> partialUpdateMeasureValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MeasureValue measureValue
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeasureValue partially : {}, {}", id, measureValue);
        if (measureValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeasureValue> result = measureValueService.partialUpdate(measureValue);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureValue.getId().toString())
        );
    }

    /**
     * {@code GET  /measure-values} : get all the measureValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measureValues in body.
     */
    @GetMapping("/measure-values")
    public ResponseEntity<List<MeasureValue>> getAllMeasureValues(MeasureValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MeasureValues by criteria: {}", criteria);
        Page<MeasureValue> page = measureValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /measure-values/count} : count all the measureValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/measure-values/count")
    public ResponseEntity<Long> countMeasureValues(MeasureValueCriteria criteria) {
        log.debug("REST request to count MeasureValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(measureValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /measure-values/:id} : get the "id" measureValue.
     *
     * @param id the id of the measureValue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measureValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measure-values/{id}")
    public ResponseEntity<MeasureValue> getMeasureValue(@PathVariable Long id) {
        log.debug("REST request to get MeasureValue : {}", id);
        Optional<MeasureValue> measureValue = measureValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measureValue);
    }

    /**
     * {@code DELETE  /measure-values/:id} : delete the "id" measureValue.
     *
     * @param id the id of the measureValue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measure-values/{id}")
    public ResponseEntity<Void> deleteMeasureValue(@PathVariable Long id) {
        log.debug("REST request to delete MeasureValue : {}", id);
        measureValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
