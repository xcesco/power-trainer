package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.repository.MisurationRepository;
import com.abubusoft.powertrainer.service.MisurationQueryService;
import com.abubusoft.powertrainer.service.MisurationService;
import com.abubusoft.powertrainer.service.criteria.MisurationCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationDTO;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Misuration}.
 */
@RestController
@RequestMapping("/api")
public class MisurationResource {

    private final Logger log = LoggerFactory.getLogger(MisurationResource.class);

    private static final String ENTITY_NAME = "misuration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MisurationService misurationService;

    private final MisurationRepository misurationRepository;

    private final MisurationQueryService misurationQueryService;

    public MisurationResource(
        MisurationService misurationService,
        MisurationRepository misurationRepository,
        MisurationQueryService misurationQueryService
    ) {
        this.misurationService = misurationService;
        this.misurationRepository = misurationRepository;
        this.misurationQueryService = misurationQueryService;
    }

    /**
     * {@code POST  /misurations} : Create a new misuration.
     *
     * @param misurationDTO the misurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new misurationDTO, or with status {@code 400 (Bad Request)} if the misuration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/misurations")
    public ResponseEntity<MisurationDTO> createMisuration(@Valid @RequestBody MisurationDTO misurationDTO) throws URISyntaxException {
        log.debug("REST request to save Misuration : {}", misurationDTO);
        if (misurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new misuration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MisurationDTO result = misurationService.save(misurationDTO);
        return ResponseEntity
            .created(new URI("/api/misurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /misurations/:id} : Updates an existing misuration.
     *
     * @param id the id of the misurationDTO to save.
     * @param misurationDTO the misurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misurationDTO,
     * or with status {@code 400 (Bad Request)} if the misurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the misurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/misurations/{id}")
    public ResponseEntity<MisurationDTO> updateMisuration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MisurationDTO misurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Misuration : {}, {}", id, misurationDTO);
        if (misurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MisurationDTO result = misurationService.save(misurationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /misurations/:id} : Partial updates given fields of an existing misuration, field will ignore if it is null
     *
     * @param id the id of the misurationDTO to save.
     * @param misurationDTO the misurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misurationDTO,
     * or with status {@code 400 (Bad Request)} if the misurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the misurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the misurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/misurations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MisurationDTO> partialUpdateMisuration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MisurationDTO misurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Misuration partially : {}, {}", id, misurationDTO);
        if (misurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MisurationDTO> result = misurationService.partialUpdate(misurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /misurations} : get all the misurations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of misurations in body.
     */
    @GetMapping("/misurations")
    public ResponseEntity<List<MisurationDTO>> getAllMisurations(MisurationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Misurations by criteria: {}", criteria);
        Page<MisurationDTO> page = misurationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /misurations/count} : count all the misurations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/misurations/count")
    public ResponseEntity<Long> countMisurations(MisurationCriteria criteria) {
        log.debug("REST request to count Misurations by criteria: {}", criteria);
        return ResponseEntity.ok().body(misurationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /misurations/:id} : get the "id" misuration.
     *
     * @param id the id of the misurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the misurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/misurations/{id}")
    public ResponseEntity<MisurationDTO> getMisuration(@PathVariable Long id) {
        log.debug("REST request to get Misuration : {}", id);
        Optional<MisurationDTO> misurationDTO = misurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(misurationDTO);
    }

    /**
     * {@code DELETE  /misurations/:id} : delete the "id" misuration.
     *
     * @param id the id of the misurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/misurations/{id}")
    public ResponseEntity<Void> deleteMisuration(@PathVariable Long id) {
        log.debug("REST request to delete Misuration : {}", id);
        misurationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
