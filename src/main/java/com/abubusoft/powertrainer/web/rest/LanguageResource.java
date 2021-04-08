package com.abubusoft.powertrainer.web.rest;

import com.abubusoft.powertrainer.domain.Language;
import com.abubusoft.powertrainer.repository.LanguageRepository;
import com.abubusoft.powertrainer.service.LanguageQueryService;
import com.abubusoft.powertrainer.service.LanguageService;
import com.abubusoft.powertrainer.service.criteria.LanguageCriteria;
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
 * REST controller for managing {@link com.abubusoft.powertrainer.domain.Language}.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    private static final String ENTITY_NAME = "language";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguageService languageService;

    private final LanguageRepository languageRepository;

    private final LanguageQueryService languageQueryService;

    public LanguageResource(
        LanguageService languageService,
        LanguageRepository languageRepository,
        LanguageQueryService languageQueryService
    ) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.languageQueryService = languageQueryService;
    }

    /**
     * {@code POST  /languages} : Create a new language.
     *
     * @param language the language to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new language, or with status {@code 400 (Bad Request)} if the language has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/languages")
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            throw new BadRequestAlertException("A new language cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Language result = languageService.save(language);
        return ResponseEntity
            .created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /languages/:id} : Updates an existing language.
     *
     * @param id the id of the language to save.
     * @param language the language to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated language,
     * or with status {@code 400 (Bad Request)} if the language is not valid,
     * or with status {@code 500 (Internal Server Error)} if the language couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/languages/{id}")
    public ResponseEntity<Language> updateLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to update Language : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Language result = languageService.save(language);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, language.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /languages/:id} : Partial updates given fields of an existing language, field will ignore if it is null
     *
     * @param id the id of the language to save.
     * @param language the language to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated language,
     * or with status {@code 400 (Bad Request)} if the language is not valid,
     * or with status {@code 404 (Not Found)} if the language is not found,
     * or with status {@code 500 (Internal Server Error)} if the language couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/languages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Language> partialUpdateLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Language language
    ) throws URISyntaxException {
        log.debug("REST request to partial update Language partially : {}, {}", id, language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Language> result = languageService.partialUpdate(language);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, language.getId().toString())
        );
    }

    /**
     * {@code GET  /languages} : get all the languages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of languages in body.
     */
    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getAllLanguages(LanguageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Languages by criteria: {}", criteria);
        Page<Language> page = languageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /languages/count} : count all the languages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/languages/count")
    public ResponseEntity<Long> countLanguages(LanguageCriteria criteria) {
        log.debug("REST request to count Languages by criteria: {}", criteria);
        return ResponseEntity.ok().body(languageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /languages/:id} : get the "id" language.
     *
     * @param id the id of the language to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the language, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/languages/{id}")
    public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        Optional<Language> language = languageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(language);
    }

    /**
     * {@code DELETE  /languages/:id} : delete the "id" language.
     *
     * @param id the id of the language to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/languages/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
