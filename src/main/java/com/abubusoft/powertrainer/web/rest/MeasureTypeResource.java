package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.MeasureType;
import com.abubusoft.powertrainer.repository.MeasureTypeRepository;
import com.abubusoft.powertrainer.service.MeasureTypeQueryService;
import com.abubusoft.powertrainer.service.MeasureTypeService;
import com.abubusoft.powertrainer.service.criteria.MeasureTypeCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.MeasureType}.
 */
@RestController
@RequestMapping("/api")
public class MeasureTypeResource {

    private final Logger log = LoggerFactory.getLogger(MeasureTypeResource.class);

    private static final String ENTITY_NAME = "measureType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasureTypeService measureTypeService;

    private final MeasureTypeRepository measureTypeRepository;

    private final MeasureTypeQueryService measureTypeQueryService;

    public MeasureTypeResource(
        MeasureTypeService measureTypeService,
        MeasureTypeRepository measureTypeRepository,
        MeasureTypeQueryService measureTypeQueryService
    ) {
        this.measureTypeService = measureTypeService;
        this.measureTypeRepository = measureTypeRepository;
        this.measureTypeQueryService = measureTypeQueryService;
    }

    /**
     * {@code POST  /measure-types} : Create a new measureType.
     *
     * @param measureType the measureType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measureType, or with status {@code 400 (Bad Request)} if the measureType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measure-types")
    public ResponseEntity<MeasureType> createMeasureType(@Valid @RequestBody MeasureType measureType) throws URISyntaxException {
        log.debug("REST request to save MeasureType : {}", measureType);
        if (measureType.getId() != null) {
            throw new BadRequestAlertException("A new measureType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeasureType result = measureTypeService.save(measureType);
        return ResponseEntity
            .created(new URI("/api/measure-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measure-types/:id} : Updates an existing measureType.
     *
     * @param id the id of the measureType to save.
     * @param measureType the measureType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureType,
     * or with status {@code 400 (Bad Request)} if the measureType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measureType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measure-types/{id}")
    public ResponseEntity<MeasureType> updateMeasureType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MeasureType measureType
    ) throws URISyntaxException {
        log.debug("REST request to update MeasureType : {}, {}", id, measureType);
        if (measureType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeasureType result = measureTypeService.save(measureType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /measure-types/:id} : Partial updates given fields of an existing measureType, field will ignore if it is null
     *
     * @param id the id of the measureType to save.
     * @param measureType the measureType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measureType,
     * or with status {@code 400 (Bad Request)} if the measureType is not valid,
     * or with status {@code 404 (Not Found)} if the measureType is not found,
     * or with status {@code 500 (Internal Server Error)} if the measureType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/measure-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MeasureType> partialUpdateMeasureType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MeasureType measureType
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeasureType partially : {}, {}", id, measureType);
        if (measureType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, measureType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!measureTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeasureType> result = measureTypeService.partialUpdate(measureType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, measureType.getId().toString())
        );
    }

    /**
     * {@code GET  /measure-types} : get all the measureTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measureTypes in body.
     */
    @GetMapping("/measure-types")
    public ResponseEntity<List<MeasureType>> getAllMeasureTypes(MeasureTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MeasureTypes by criteria: {}", criteria);
        Page<MeasureType> page = measureTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /measure-types/count} : count all the measureTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/measure-types/count")
    public ResponseEntity<Long> countMeasureTypes(MeasureTypeCriteria criteria) {
        log.debug("REST request to count MeasureTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(measureTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /measure-types/:id} : get the "id" measureType.
     *
     * @param id the id of the measureType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measureType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measure-types/{id}")
    public ResponseEntity<MeasureType> getMeasureType(@PathVariable Long id) {
        log.debug("REST request to get MeasureType : {}", id);
        Optional<MeasureType> measureType = measureTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(measureType);
    }

    /**
     * {@code DELETE  /measure-types/:id} : delete the "id" measureType.
     *
     * @param id the id of the measureType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measure-types/{id}")
    public ResponseEntity<Void> deleteMeasureType(@PathVariable Long id) {
        log.debug("REST request to delete MeasureType : {}", id);
        measureTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
