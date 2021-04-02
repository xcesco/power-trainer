package com.abubusoft.powertrainer.web.rest;

import static com.abubusoft.powertrainer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.MeasureValue;
import com.abubusoft.powertrainer.repository.MeasureValueRepository;
import com.abubusoft.powertrainer.service.criteria.MeasureValueCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MeasureValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeasureValueResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final Integer SMALLER_VALUE = 1 - 1;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/measure-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeasureValueRepository measureValueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasureValueMockMvc;

    private MeasureValue measureValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureValue createEntity(EntityManager em) {
        MeasureValue measureValue = new MeasureValue().uuid(DEFAULT_UUID).date(DEFAULT_DATE).value(DEFAULT_VALUE).note(DEFAULT_NOTE);
        return measureValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureValue createUpdatedEntity(EntityManager em) {
        MeasureValue measureValue = new MeasureValue().uuid(UPDATED_UUID).date(UPDATED_DATE).value(UPDATED_VALUE).note(UPDATED_NOTE);
        return measureValue;
    }

    @BeforeEach
    public void initTest() {
        measureValue = createEntity(em);
    }

    @Test
    @Transactional
    void createMeasureValue() throws Exception {
        int databaseSizeBeforeCreate = measureValueRepository.findAll().size();
        // Create the MeasureValue
        restMeasureValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isCreated());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeCreate + 1);
        MeasureValue testMeasureValue = measureValueList.get(measureValueList.size() - 1);
        assertThat(testMeasureValue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMeasureValue.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMeasureValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMeasureValue.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createMeasureValueWithExistingId() throws Exception {
        // Create the MeasureValue with an existing ID
        measureValue.setId(1L);

        int databaseSizeBeforeCreate = measureValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isBadRequest());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureValueRepository.findAll().size();
        // set the field null
        measureValue.setUuid(null);

        // Create the MeasureValue, which fails.

        restMeasureValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isBadRequest());

        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureValueRepository.findAll().size();
        // set the field null
        measureValue.setDate(null);

        // Create the MeasureValue, which fails.

        restMeasureValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isBadRequest());

        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureValueRepository.findAll().size();
        // set the field null
        measureValue.setValue(null);

        // Create the MeasureValue, which fails.

        restMeasureValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isBadRequest());

        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasureValues() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measureValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    void getMeasureValue() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get the measureValue
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL_ID, measureValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measureValue.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    void getMeasureValuesByIdFiltering() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        Long id = measureValue.getId();

        defaultMeasureValueShouldBeFound("id.equals=" + id);
        defaultMeasureValueShouldNotBeFound("id.notEquals=" + id);

        defaultMeasureValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeasureValueShouldNotBeFound("id.greaterThan=" + id);

        defaultMeasureValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeasureValueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where uuid equals to DEFAULT_UUID
        defaultMeasureValueShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the measureValueList where uuid equals to UPDATED_UUID
        defaultMeasureValueShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where uuid not equals to DEFAULT_UUID
        defaultMeasureValueShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the measureValueList where uuid not equals to UPDATED_UUID
        defaultMeasureValueShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultMeasureValueShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the measureValueList where uuid equals to UPDATED_UUID
        defaultMeasureValueShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where uuid is not null
        defaultMeasureValueShouldBeFound("uuid.specified=true");

        // Get all the measureValueList where uuid is null
        defaultMeasureValueShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date equals to DEFAULT_DATE
        defaultMeasureValueShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the measureValueList where date equals to UPDATED_DATE
        defaultMeasureValueShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date not equals to DEFAULT_DATE
        defaultMeasureValueShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the measureValueList where date not equals to UPDATED_DATE
        defaultMeasureValueShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMeasureValueShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the measureValueList where date equals to UPDATED_DATE
        defaultMeasureValueShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date is not null
        defaultMeasureValueShouldBeFound("date.specified=true");

        // Get all the measureValueList where date is null
        defaultMeasureValueShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date is greater than or equal to DEFAULT_DATE
        defaultMeasureValueShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the measureValueList where date is greater than or equal to UPDATED_DATE
        defaultMeasureValueShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date is less than or equal to DEFAULT_DATE
        defaultMeasureValueShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the measureValueList where date is less than or equal to SMALLER_DATE
        defaultMeasureValueShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date is less than DEFAULT_DATE
        defaultMeasureValueShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the measureValueList where date is less than UPDATED_DATE
        defaultMeasureValueShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where date is greater than DEFAULT_DATE
        defaultMeasureValueShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the measureValueList where date is greater than SMALLER_DATE
        defaultMeasureValueShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value equals to DEFAULT_VALUE
        defaultMeasureValueShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the measureValueList where value equals to UPDATED_VALUE
        defaultMeasureValueShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value not equals to DEFAULT_VALUE
        defaultMeasureValueShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the measureValueList where value not equals to UPDATED_VALUE
        defaultMeasureValueShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultMeasureValueShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the measureValueList where value equals to UPDATED_VALUE
        defaultMeasureValueShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value is not null
        defaultMeasureValueShouldBeFound("value.specified=true");

        // Get all the measureValueList where value is null
        defaultMeasureValueShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value is greater than or equal to DEFAULT_VALUE
        defaultMeasureValueShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the measureValueList where value is greater than or equal to UPDATED_VALUE
        defaultMeasureValueShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value is less than or equal to DEFAULT_VALUE
        defaultMeasureValueShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the measureValueList where value is less than or equal to SMALLER_VALUE
        defaultMeasureValueShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value is less than DEFAULT_VALUE
        defaultMeasureValueShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the measureValueList where value is less than UPDATED_VALUE
        defaultMeasureValueShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMeasureValuesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        // Get all the measureValueList where value is greater than DEFAULT_VALUE
        defaultMeasureValueShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the measureValueList where value is greater than SMALLER_VALUE
        defaultMeasureValueShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeasureValueShouldBeFound(String filter) throws Exception {
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measureValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));

        // Check, that the count call also returns 1
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeasureValueShouldNotBeFound(String filter) throws Exception {
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeasureValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeasureValue() throws Exception {
        // Get the measureValue
        restMeasureValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeasureValue() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();

        // Update the measureValue
        MeasureValue updatedMeasureValue = measureValueRepository.findById(measureValue.getId()).get();
        // Disconnect from session so that the updates on updatedMeasureValue are not directly saved in db
        em.detach(updatedMeasureValue);
        updatedMeasureValue.uuid(UPDATED_UUID).date(UPDATED_DATE).value(UPDATED_VALUE).note(UPDATED_NOTE);

        restMeasureValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeasureValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeasureValue))
            )
            .andExpect(status().isOk());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
        MeasureValue testMeasureValue = measureValueList.get(measureValueList.size() - 1);
        assertThat(testMeasureValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureValue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMeasureValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMeasureValue.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measureValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeasureValueWithPatch() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();

        // Update the measureValue using partial update
        MeasureValue partialUpdatedMeasureValue = new MeasureValue();
        partialUpdatedMeasureValue.setId(measureValue.getId());

        partialUpdatedMeasureValue.uuid(UPDATED_UUID).value(UPDATED_VALUE).note(UPDATED_NOTE);

        restMeasureValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureValue))
            )
            .andExpect(status().isOk());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
        MeasureValue testMeasureValue = measureValueList.get(measureValueList.size() - 1);
        assertThat(testMeasureValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureValue.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMeasureValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMeasureValue.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateMeasureValueWithPatch() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();

        // Update the measureValue using partial update
        MeasureValue partialUpdatedMeasureValue = new MeasureValue();
        partialUpdatedMeasureValue.setId(measureValue.getId());

        partialUpdatedMeasureValue.uuid(UPDATED_UUID).date(UPDATED_DATE).value(UPDATED_VALUE).note(UPDATED_NOTE);

        restMeasureValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureValue))
            )
            .andExpect(status().isOk());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
        MeasureValue testMeasureValue = measureValueList.get(measureValueList.size() - 1);
        assertThat(testMeasureValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureValue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMeasureValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMeasureValue.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measureValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasureValue() throws Exception {
        int databaseSizeBeforeUpdate = measureValueRepository.findAll().size();
        measureValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(measureValue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureValue in the database
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeasureValue() throws Exception {
        // Initialize the database
        measureValueRepository.saveAndFlush(measureValue);

        int databaseSizeBeforeDelete = measureValueRepository.findAll().size();

        // Delete the measureValue
        restMeasureValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, measureValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeasureValue> measureValueList = measureValueRepository.findAll();
        assertThat(measureValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
