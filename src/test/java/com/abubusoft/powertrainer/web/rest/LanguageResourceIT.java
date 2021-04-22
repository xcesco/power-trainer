package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Language;
import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.LanguageRepository;
import com.abubusoft.powertrainer.service.criteria.LanguageCriteria;
import com.abubusoft.powertrainer.service.dto.LanguageDTO;
import com.abubusoft.powertrainer.service.mapper.LanguageMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanguageResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageMockMvc;

    private Language language;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createEntity(EntityManager em) {
        Language language = new Language().code(DEFAULT_CODE).name(DEFAULT_NAME);
        return language;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Language createUpdatedEntity(EntityManager em) {
        Language language = new Language().code(UPDATED_CODE).name(UPDATED_NAME);
        return language;
    }

    @BeforeEach
    public void initTest() {
        language = createEntity(em);
    }

    @Test
    @Transactional
    void createLanguage() throws Exception {
        int databaseSizeBeforeCreate = languageRepository.findAll().size();
        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isCreated());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate + 1);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLanguage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLanguageWithExistingId() throws Exception {
        // Create the Language with an existing ID
        language.setId(1L);
        LanguageDTO languageDTO = languageMapper.toDto(language);

        int databaseSizeBeforeCreate = languageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setCode(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = languageRepository.findAll().size();
        // set the field null
        language.setName(null);

        // Create the Language, which fails.
        LanguageDTO languageDTO = languageMapper.toDto(language);

        restLanguageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getLanguagesByIdFiltering() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        Long id = language.getId();

        defaultLanguageShouldBeFound("id.equals=" + id);
        defaultLanguageShouldNotBeFound("id.notEquals=" + id);

        defaultLanguageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLanguageShouldNotBeFound("id.greaterThan=" + id);

        defaultLanguageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLanguageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code equals to DEFAULT_CODE
        defaultLanguageShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the languageList where code equals to UPDATED_CODE
        defaultLanguageShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code not equals to DEFAULT_CODE
        defaultLanguageShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the languageList where code not equals to UPDATED_CODE
        defaultLanguageShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code in DEFAULT_CODE or UPDATED_CODE
        defaultLanguageShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the languageList where code equals to UPDATED_CODE
        defaultLanguageShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code is not null
        defaultLanguageShouldBeFound("code.specified=true");

        // Get all the languageList where code is null
        defaultLanguageShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code contains DEFAULT_CODE
        defaultLanguageShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the languageList where code contains UPDATED_CODE
        defaultLanguageShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where code does not contain DEFAULT_CODE
        defaultLanguageShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the languageList where code does not contain UPDATED_CODE
        defaultLanguageShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLanguagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name equals to DEFAULT_NAME
        defaultLanguageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the languageList where name equals to UPDATED_NAME
        defaultLanguageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name not equals to DEFAULT_NAME
        defaultLanguageShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the languageList where name not equals to UPDATED_NAME
        defaultLanguageShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLanguageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the languageList where name equals to UPDATED_NAME
        defaultLanguageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name is not null
        defaultLanguageShouldBeFound("name.specified=true");

        // Get all the languageList where name is null
        defaultLanguageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllLanguagesByNameContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name contains DEFAULT_NAME
        defaultLanguageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the languageList where name contains UPDATED_NAME
        defaultLanguageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList where name does not contain DEFAULT_NAME
        defaultLanguageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the languageList where name does not contain UPDATED_NAME
        defaultLanguageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanguagesByTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);
        Translation translation = TranslationResourceIT.createEntity(em);
        em.persist(translation);
        em.flush();
        language.addTranslation(translation);
        languageRepository.saveAndFlush(language);
        Long translationId = translation.getId();

        // Get all the languageList where translation equals to translationId
        defaultLanguageShouldBeFound("translationId.equals=" + translationId);

        // Get all the languageList where translation equals to (translationId + 1)
        defaultLanguageShouldNotBeFound("translationId.equals=" + (translationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLanguageShouldBeFound(String filter) throws Exception {
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLanguageShouldNotBeFound(String filter) throws Exception {
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).get();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage.code(UPDATED_CODE).name(UPDATED_NAME);
        LanguageDTO languageDTO = languageMapper.toDto(updatedLanguage);

        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(languageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.name(UPDATED_NAME);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLanguageWithPatch() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language using partial update
        Language partialUpdatedLanguage = new Language();
        partialUpdatedLanguage.setId(language.getId());

        partialUpdatedLanguage.code(UPDATED_CODE).name(UPDATED_NAME);

        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);
        assertThat(testLanguage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // Create the Language
        LanguageDTO languageDTO = languageMapper.toDto(language);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(languageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Delete the language
        restLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, language.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
