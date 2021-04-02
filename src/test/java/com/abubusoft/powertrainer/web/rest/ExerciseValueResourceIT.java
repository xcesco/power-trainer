package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseValueCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link ExerciseValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseValueResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final Integer SMALLER_VALUE = 1 - 1;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/exercise-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseValueRepository exerciseValueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseValueMockMvc;

    private ExerciseValue exerciseValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseValue createEntity(EntityManager em) {
        ExerciseValue exerciseValue = new ExerciseValue().uuid(DEFAULT_UUID).value(DEFAULT_VALUE).date(DEFAULT_DATE);
        return exerciseValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseValue createUpdatedEntity(EntityManager em) {
        ExerciseValue exerciseValue = new ExerciseValue().uuid(UPDATED_UUID).value(UPDATED_VALUE).date(UPDATED_DATE);
        return exerciseValue;
    }

    @BeforeEach
    public void initTest() {
        exerciseValue = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseValue() throws Exception {
        int databaseSizeBeforeCreate = exerciseValueRepository.findAll().size();
        // Create the ExerciseValue
        restExerciseValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isCreated());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testExerciseValue.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createExerciseValueWithExistingId() throws Exception {
        // Create the ExerciseValue with an existing ID
        exerciseValue.setId(1L);

        int databaseSizeBeforeCreate = exerciseValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isBadRequest());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setUuid(null);

        // Create the ExerciseValue, which fails.

        restExerciseValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setValue(null);

        // Create the ExerciseValue, which fails.

        restExerciseValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setDate(null);

        // Create the ExerciseValue, which fails.

        restExerciseValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExerciseValues() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getExerciseValue() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get the exerciseValue
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseValue.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getExerciseValuesByIdFiltering() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        Long id = exerciseValue.getId();

        defaultExerciseValueShouldBeFound("id.equals=" + id);
        defaultExerciseValueShouldNotBeFound("id.notEquals=" + id);

        defaultExerciseValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExerciseValueShouldNotBeFound("id.greaterThan=" + id);

        defaultExerciseValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExerciseValueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where uuid equals to DEFAULT_UUID
        defaultExerciseValueShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the exerciseValueList where uuid equals to UPDATED_UUID
        defaultExerciseValueShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where uuid not equals to DEFAULT_UUID
        defaultExerciseValueShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the exerciseValueList where uuid not equals to UPDATED_UUID
        defaultExerciseValueShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultExerciseValueShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the exerciseValueList where uuid equals to UPDATED_UUID
        defaultExerciseValueShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where uuid is not null
        defaultExerciseValueShouldBeFound("uuid.specified=true");

        // Get all the exerciseValueList where uuid is null
        defaultExerciseValueShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value equals to DEFAULT_VALUE
        defaultExerciseValueShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value equals to UPDATED_VALUE
        defaultExerciseValueShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value not equals to DEFAULT_VALUE
        defaultExerciseValueShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value not equals to UPDATED_VALUE
        defaultExerciseValueShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultExerciseValueShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the exerciseValueList where value equals to UPDATED_VALUE
        defaultExerciseValueShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value is not null
        defaultExerciseValueShouldBeFound("value.specified=true");

        // Get all the exerciseValueList where value is null
        defaultExerciseValueShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value is greater than or equal to DEFAULT_VALUE
        defaultExerciseValueShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value is greater than or equal to UPDATED_VALUE
        defaultExerciseValueShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value is less than or equal to DEFAULT_VALUE
        defaultExerciseValueShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value is less than or equal to SMALLER_VALUE
        defaultExerciseValueShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value is less than DEFAULT_VALUE
        defaultExerciseValueShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value is less than UPDATED_VALUE
        defaultExerciseValueShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where value is greater than DEFAULT_VALUE
        defaultExerciseValueShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the exerciseValueList where value is greater than SMALLER_VALUE
        defaultExerciseValueShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date equals to DEFAULT_DATE
        defaultExerciseValueShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date equals to UPDATED_DATE
        defaultExerciseValueShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date not equals to DEFAULT_DATE
        defaultExerciseValueShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date not equals to UPDATED_DATE
        defaultExerciseValueShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date in DEFAULT_DATE or UPDATED_DATE
        defaultExerciseValueShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the exerciseValueList where date equals to UPDATED_DATE
        defaultExerciseValueShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date is not null
        defaultExerciseValueShouldBeFound("date.specified=true");

        // Get all the exerciseValueList where date is null
        defaultExerciseValueShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date is greater than or equal to DEFAULT_DATE
        defaultExerciseValueShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date is greater than or equal to UPDATED_DATE
        defaultExerciseValueShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date is less than or equal to DEFAULT_DATE
        defaultExerciseValueShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date is less than or equal to SMALLER_DATE
        defaultExerciseValueShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date is less than DEFAULT_DATE
        defaultExerciseValueShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date is less than UPDATED_DATE
        defaultExerciseValueShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where date is greater than DEFAULT_DATE
        defaultExerciseValueShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the exerciseValueList where date is greater than SMALLER_DATE
        defaultExerciseValueShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseValueShouldBeFound(String filter) throws Exception {
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseValueShouldNotBeFound(String filter) throws Exception {
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExerciseValue() throws Exception {
        // Get the exerciseValue
        restExerciseValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseValue() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();

        // Update the exerciseValue
        ExerciseValue updatedExerciseValue = exerciseValueRepository.findById(exerciseValue.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseValue are not directly saved in db
        em.detach(updatedExerciseValue);
        updatedExerciseValue.uuid(UPDATED_UUID).value(UPDATED_VALUE).date(UPDATED_DATE);

        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseValue))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseValueWithPatch() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();

        // Update the exerciseValue using partial update
        ExerciseValue partialUpdatedExerciseValue = new ExerciseValue();
        partialUpdatedExerciseValue.setId(exerciseValue.getId());

        partialUpdatedExerciseValue.uuid(UPDATED_UUID).value(UPDATED_VALUE).date(UPDATED_DATE);

        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseValue))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExerciseValueWithPatch() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();

        // Update the exerciseValue using partial update
        ExerciseValue partialUpdatedExerciseValue = new ExerciseValue();
        partialUpdatedExerciseValue.setId(exerciseValue.getId());

        partialUpdatedExerciseValue.uuid(UPDATED_UUID).value(UPDATED_VALUE).date(UPDATED_DATE);

        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseValue))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseValue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseValue() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        int databaseSizeBeforeDelete = exerciseValueRepository.findAll().size();

        // Delete the exerciseValue
        restExerciseValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
