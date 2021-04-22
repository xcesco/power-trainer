package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Language;
import com.abubusoft.powertrainer.domain.Translation;
import com.abubusoft.powertrainer.repository.TranslationRepository;
import com.abubusoft.powertrainer.service.criteria.TranslationCriteria;
import com.abubusoft.powertrainer.service.dto.TranslationDTO;
import com.abubusoft.powertrainer.service.mapper.TranslationMapper;
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
 * Integration tests for the {@link TranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TranslationResourceIT {

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_UUID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private TranslationMapper translationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTranslationMockMvc;

    private Translation translation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translation createEntity(EntityManager em) {
        Translation translation = new Translation().entityType(DEFAULT_ENTITY_TYPE).entityUuid(DEFAULT_ENTITY_UUID).value(DEFAULT_VALUE);
        return translation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Translation createUpdatedEntity(EntityManager em) {
        Translation translation = new Translation().entityType(UPDATED_ENTITY_TYPE).entityUuid(UPDATED_ENTITY_UUID).value(UPDATED_VALUE);
        return translation;
    }

    @BeforeEach
    public void initTest() {
        translation = createEntity(em);
    }

    @Test
    @Transactional
    void createTranslation() throws Exception {
        int databaseSizeBeforeCreate = translationRepository.findAll().size();
        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);
        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate + 1);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testTranslation.getEntityUuid()).isEqualTo(DEFAULT_ENTITY_UUID);
        assertThat(testTranslation.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTranslationWithExistingId() throws Exception {
        // Create the Translation with an existing ID
        translation.setId(1L);
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        int databaseSizeBeforeCreate = translationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = translationRepository.findAll().size();
        // set the field null
        translation.setEntityType(null);

        // Create the Translation, which fails.
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = translationRepository.findAll().size();
        // set the field null
        translation.setEntityUuid(null);

        // Create the Translation, which fails.
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = translationRepository.findAll().size();
        // set the field null
        translation.setValue(null);

        // Create the Translation, which fails.
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        restTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTranslations() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translation.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityUuid").value(hasItem(DEFAULT_ENTITY_UUID)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get the translation
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, translation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(translation.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityUuid").value(DEFAULT_ENTITY_UUID))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        Long id = translation.getId();

        defaultTranslationShouldBeFound("id.equals=" + id);
        defaultTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType equals to DEFAULT_ENTITY_TYPE
        defaultTranslationShouldBeFound("entityType.equals=" + DEFAULT_ENTITY_TYPE);

        // Get all the translationList where entityType equals to UPDATED_ENTITY_TYPE
        defaultTranslationShouldNotBeFound("entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType not equals to DEFAULT_ENTITY_TYPE
        defaultTranslationShouldNotBeFound("entityType.notEquals=" + DEFAULT_ENTITY_TYPE);

        // Get all the translationList where entityType not equals to UPDATED_ENTITY_TYPE
        defaultTranslationShouldBeFound("entityType.notEquals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType in DEFAULT_ENTITY_TYPE or UPDATED_ENTITY_TYPE
        defaultTranslationShouldBeFound("entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE);

        // Get all the translationList where entityType equals to UPDATED_ENTITY_TYPE
        defaultTranslationShouldNotBeFound("entityType.in=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType is not null
        defaultTranslationShouldBeFound("entityType.specified=true");

        // Get all the translationList where entityType is null
        defaultTranslationShouldNotBeFound("entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType contains DEFAULT_ENTITY_TYPE
        defaultTranslationShouldBeFound("entityType.contains=" + DEFAULT_ENTITY_TYPE);

        // Get all the translationList where entityType contains UPDATED_ENTITY_TYPE
        defaultTranslationShouldNotBeFound("entityType.contains=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityType does not contain DEFAULT_ENTITY_TYPE
        defaultTranslationShouldNotBeFound("entityType.doesNotContain=" + DEFAULT_ENTITY_TYPE);

        // Get all the translationList where entityType does not contain UPDATED_ENTITY_TYPE
        defaultTranslationShouldBeFound("entityType.doesNotContain=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid equals to DEFAULT_ENTITY_UUID
        defaultTranslationShouldBeFound("entityUuid.equals=" + DEFAULT_ENTITY_UUID);

        // Get all the translationList where entityUuid equals to UPDATED_ENTITY_UUID
        defaultTranslationShouldNotBeFound("entityUuid.equals=" + UPDATED_ENTITY_UUID);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid not equals to DEFAULT_ENTITY_UUID
        defaultTranslationShouldNotBeFound("entityUuid.notEquals=" + DEFAULT_ENTITY_UUID);

        // Get all the translationList where entityUuid not equals to UPDATED_ENTITY_UUID
        defaultTranslationShouldBeFound("entityUuid.notEquals=" + UPDATED_ENTITY_UUID);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidIsInShouldWork() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid in DEFAULT_ENTITY_UUID or UPDATED_ENTITY_UUID
        defaultTranslationShouldBeFound("entityUuid.in=" + DEFAULT_ENTITY_UUID + "," + UPDATED_ENTITY_UUID);

        // Get all the translationList where entityUuid equals to UPDATED_ENTITY_UUID
        defaultTranslationShouldNotBeFound("entityUuid.in=" + UPDATED_ENTITY_UUID);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid is not null
        defaultTranslationShouldBeFound("entityUuid.specified=true");

        // Get all the translationList where entityUuid is null
        defaultTranslationShouldNotBeFound("entityUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid contains DEFAULT_ENTITY_UUID
        defaultTranslationShouldBeFound("entityUuid.contains=" + DEFAULT_ENTITY_UUID);

        // Get all the translationList where entityUuid contains UPDATED_ENTITY_UUID
        defaultTranslationShouldNotBeFound("entityUuid.contains=" + UPDATED_ENTITY_UUID);
    }

    @Test
    @Transactional
    void getAllTranslationsByEntityUuidNotContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where entityUuid does not contain DEFAULT_ENTITY_UUID
        defaultTranslationShouldNotBeFound("entityUuid.doesNotContain=" + DEFAULT_ENTITY_UUID);

        // Get all the translationList where entityUuid does not contain UPDATED_ENTITY_UUID
        defaultTranslationShouldBeFound("entityUuid.doesNotContain=" + UPDATED_ENTITY_UUID);
    }

    @Test
    @Transactional
    void getAllTranslationsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value equals to DEFAULT_VALUE
        defaultTranslationShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the translationList where value equals to UPDATED_VALUE
        defaultTranslationShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTranslationsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value not equals to DEFAULT_VALUE
        defaultTranslationShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the translationList where value not equals to UPDATED_VALUE
        defaultTranslationShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTranslationsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTranslationShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the translationList where value equals to UPDATED_VALUE
        defaultTranslationShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTranslationsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value is not null
        defaultTranslationShouldBeFound("value.specified=true");

        // Get all the translationList where value is null
        defaultTranslationShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTranslationsByValueContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value contains DEFAULT_VALUE
        defaultTranslationShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the translationList where value contains UPDATED_VALUE
        defaultTranslationShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTranslationsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        // Get all the translationList where value does not contain DEFAULT_VALUE
        defaultTranslationShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the translationList where value does not contain UPDATED_VALUE
        defaultTranslationShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTranslationsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);
        Language language = LanguageResourceIT.createEntity(em);
        em.persist(language);
        em.flush();
        translation.setLanguage(language);
        translationRepository.saveAndFlush(translation);
        Long languageId = language.getId();

        // Get all the translationList where language equals to languageId
        defaultTranslationShouldBeFound("languageId.equals=" + languageId);

        // Get all the translationList where language equals to (languageId + 1)
        defaultTranslationShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTranslationShouldBeFound(String filter) throws Exception {
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translation.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityUuid").value(hasItem(DEFAULT_ENTITY_UUID)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTranslationShouldNotBeFound(String filter) throws Exception {
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTranslation() throws Exception {
        // Get the translation
        restTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation
        Translation updatedTranslation = translationRepository.findById(translation.getId()).get();
        // Disconnect from session so that the updates on updatedTranslation are not directly saved in db
        em.detach(updatedTranslation);
        updatedTranslation.entityType(UPDATED_ENTITY_TYPE).entityUuid(UPDATED_ENTITY_UUID).value(UPDATED_VALUE);
        TranslationDTO translationDTO = translationMapper.toDto(updatedTranslation);

        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testTranslation.getEntityUuid()).isEqualTo(UPDATED_ENTITY_UUID);
        assertThat(testTranslation.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(translationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTranslationWithPatch() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation using partial update
        Translation partialUpdatedTranslation = new Translation();
        partialUpdatedTranslation.setId(translation.getId());

        partialUpdatedTranslation.value(UPDATED_VALUE);

        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranslation))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testTranslation.getEntityUuid()).isEqualTo(DEFAULT_ENTITY_UUID);
        assertThat(testTranslation.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTranslationWithPatch() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeUpdate = translationRepository.findAll().size();

        // Update the translation using partial update
        Translation partialUpdatedTranslation = new Translation();
        partialUpdatedTranslation.setId(translation.getId());

        partialUpdatedTranslation.entityType(UPDATED_ENTITY_TYPE).entityUuid(UPDATED_ENTITY_UUID).value(UPDATED_VALUE);

        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranslation))
            )
            .andExpect(status().isOk());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
        Translation testTranslation = translationList.get(translationList.size() - 1);
        assertThat(testTranslation.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testTranslation.getEntityUuid()).isEqualTo(UPDATED_ENTITY_UUID);
        assertThat(testTranslation.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, translationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTranslation() throws Exception {
        int databaseSizeBeforeUpdate = translationRepository.findAll().size();
        translation.setId(count.incrementAndGet());

        // Create the Translation
        TranslationDTO translationDTO = translationMapper.toDto(translation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(translationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Translation in the database
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTranslation() throws Exception {
        // Initialize the database
        translationRepository.saveAndFlush(translation);

        int databaseSizeBeforeDelete = translationRepository.findAll().size();

        // Delete the translation
        restTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, translation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Translation> translationList = translationRepository.findAll();
        assertThat(translationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
