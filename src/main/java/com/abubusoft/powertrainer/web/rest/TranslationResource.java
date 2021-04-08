package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.TranslationRepository;
import com.abubusoft.powertrainer.service.TranslationQueryService;
import com.abubusoft.powertrainer.service.TranslationService;
import com.abubusoft.powertrainer.service.criteria.TranslationCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Translation}.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);

    private static final String ENTITY_NAME = "translation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TranslationService translationService;

    private final TranslationRepository translationRepository;

    private final TranslationQueryService translationQueryService;

    public TranslationResource(
        TranslationService translationService,
        TranslationRepository translationRepository,
        TranslationQueryService translationQueryService
    ) {
        this.translationService = translationService;
        this.translationRepository = translationRepository;
        this.translationQueryService = translationQueryService;
    }

    /**
     * {@code POST  /translations} : Create a new translation.
     *
     * @param translation the translation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new translation, or with status {@code 400 (Bad Request)} if the translation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/translations")
    public ResponseEntity<Translation> createTranslation(@Valid @RequestBody Translation translation) throws URISyntaxException {
        log.debug("REST request to save Translation : {}", translation);
        if (translation.getId() != null) {
            throw new BadRequestAlertException("A new translation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Translation result = translationService.save(translation);
        return ResponseEntity
            .created(new URI("/api/translations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /translations/:id} : Updates an existing translation.
     *
     * @param id the id of the translation to save.
     * @param translation the translation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated translation,
     * or with status {@code 400 (Bad Request)} if the translation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the translation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/translations/{id}")
    public ResponseEntity<Translation> updateTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Translation translation
    ) throws URISyntaxException {
        log.debug("REST request to update Translation : {}, {}", id, translation);
        if (translation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, translation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!translationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Translation result = translationService.save(translation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, translation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /translations/:id} : Partial updates given fields of an existing translation, field will ignore if it is null
     *
     * @param id the id of the translation to save.
     * @param translation the translation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated translation,
     * or with status {@code 400 (Bad Request)} if the translation is not valid,
     * or with status {@code 404 (Not Found)} if the translation is not found,
     * or with status {@code 500 (Internal Server Error)} if the translation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/translations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Translation> partialUpdateTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Translation translation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Translation partially : {}, {}", id, translation);
        if (translation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, translation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!translationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Translation> result = translationService.partialUpdate(translation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, translation.getId().toString())
        );
    }

    /**
     * {@code GET  /translations} : get all the translations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of translations in body.
     */
    @GetMapping("/translations")
    public ResponseEntity<List<Translation>> getAllTranslations(TranslationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Translations by criteria: {}", criteria);
        Page<Translation> page = translationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /translations/count} : count all the translations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/translations/count")
    public ResponseEntity<Long> countTranslations(TranslationCriteria criteria) {
        log.debug("REST request to count Translations by criteria: {}", criteria);
        return ResponseEntity.ok().body(translationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /translations/:id} : get the "id" translation.
     *
     * @param id the id of the translation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the translation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/translations/{id}")
    public ResponseEntity<Translation> getTranslation(@PathVariable Long id) {
        log.debug("REST request to get Translation : {}", id);
        Optional<Translation> translation = translationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(translation);
    }

    /**
     * {@code DELETE  /translations/:id} : delete the "id" translation.
     *
     * @param id the id of the translation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/translations/{id}")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        log.debug("REST request to delete Translation : {}", id);
        translationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
