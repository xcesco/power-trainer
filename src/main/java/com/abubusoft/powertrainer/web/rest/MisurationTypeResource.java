package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.MisurationTypeRepository;
import com.abubusoft.powertrainer.service.MisurationTypeQueryService;
import com.abubusoft.powertrainer.service.MisurationTypeService;
import com.abubusoft.powertrainer.service.criteria.MisurationTypeCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.MisurationType}.
 */
@RestController
@RequestMapping("/api")
public class MisurationTypeResource {

    private final Logger log = LoggerFactory.getLogger(MisurationTypeResource.class);

    private static final String ENTITY_NAME = "misurationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MisurationTypeService misurationTypeService;

    private final MisurationTypeRepository misurationTypeRepository;

    private final MisurationTypeQueryService misurationTypeQueryService;

    public MisurationTypeResource(
        MisurationTypeService misurationTypeService,
        MisurationTypeRepository misurationTypeRepository,
        MisurationTypeQueryService misurationTypeQueryService
    ) {
        this.misurationTypeService = misurationTypeService;
        this.misurationTypeRepository = misurationTypeRepository;
        this.misurationTypeQueryService = misurationTypeQueryService;
    }

    /**
     * {@code POST  /misuration-types} : Create a new misurationType.
     *
     * @param misurationTypeDTO the misurationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new misurationTypeDTO, or with status {@code 400 (Bad Request)} if the misurationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/misuration-types")
    public ResponseEntity<MisurationTypeDTO> createMisurationType(@Valid @RequestBody MisurationTypeDTO misurationTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MisurationType : {}", misurationTypeDTO);
        if (misurationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new misurationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MisurationTypeDTO result = misurationTypeService.save(misurationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/misuration-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /misuration-types/:id} : Updates an existing misurationType.
     *
     * @param id the id of the misurationTypeDTO to save.
     * @param misurationTypeDTO the misurationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misurationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the misurationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the misurationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/misuration-types/{id}")
    public ResponseEntity<MisurationTypeDTO> updateMisurationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MisurationTypeDTO misurationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MisurationType : {}, {}", id, misurationTypeDTO);
        if (misurationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misurationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misurationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MisurationTypeDTO result = misurationTypeService.save(misurationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misurationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /misuration-types/:id} : Partial updates given fields of an existing misurationType, field will ignore if it is null
     *
     * @param id the id of the misurationTypeDTO to save.
     * @param misurationTypeDTO the misurationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misurationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the misurationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the misurationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the misurationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/misuration-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MisurationTypeDTO> partialUpdateMisurationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MisurationTypeDTO misurationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MisurationType partially : {}, {}", id, misurationTypeDTO);
        if (misurationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misurationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misurationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MisurationTypeDTO> result = misurationTypeService.partialUpdate(misurationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misurationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /misuration-types} : get all the misurationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of misurationTypes in body.
     */
    @GetMapping("/misuration-types")
    public ResponseEntity<List<MisurationTypeDTO>> getAllMisurationTypes(MisurationTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MisurationTypes by criteria: {}", criteria);
        Page<MisurationTypeDTO> page = misurationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /misuration-types/count} : count all the misurationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/misuration-types/count")
    public ResponseEntity<Long> countMisurationTypes(MisurationTypeCriteria criteria) {
        log.debug("REST request to count MisurationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(misurationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /misuration-types/:id} : get the "id" misurationType.
     *
     * @param id the id of the misurationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the misurationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/misuration-types/{id}")
    public ResponseEntity<MisurationTypeDTO> getMisurationType(@PathVariable Long id) {
        log.debug("REST request to get MisurationType : {}", id);
        Optional<MisurationTypeDTO> misurationTypeDTO = misurationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(misurationTypeDTO);
    }

    /**
     * {@code DELETE  /misuration-types/:id} : delete the "id" misurationType.
     *
     * @param id the id of the misurationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/misuration-types/{id}")
    public ResponseEntity<Void> deleteMisurationType(@PathVariable Long id) {
        log.debug("REST request to delete MisurationType : {}", id);
        misurationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
