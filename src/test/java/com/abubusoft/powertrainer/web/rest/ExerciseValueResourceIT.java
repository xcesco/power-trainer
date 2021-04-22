package com.abubusoft.powertrainer.web.rest;

import static com.abubusoft.powertrainer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.repository.ExerciseValueRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseValueCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseValueDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseValueMapper;
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

/**
 * Integration tests for the {@link ExerciseValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseValueResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final UUID DEFAULT_EXERCISE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_EXERCISE_UUID = UUID.randomUUID();

    private static final String DEFAULT_EXERCISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXERCISE_VALUE = 1;
    private static final Integer UPDATED_EXERCISE_VALUE = 2;
    private static final Integer SMALLER_EXERCISE_VALUE = 1 - 1;

    private static final ValueType DEFAULT_EXERCISE_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_EXERCISE_VALUE_TYPE = ValueType.WEIGHT;

    private static final String ENTITY_API_URL = "/api/exercise-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseValueRepository exerciseValueRepository;

    @Autowired
    private ExerciseValueMapper exerciseValueMapper;

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
        ExerciseValue exerciseValue = new ExerciseValue()
            .uuid(DEFAULT_UUID)
            .date(DEFAULT_DATE)
            .exerciseUuid(DEFAULT_EXERCISE_UUID)
            .exerciseName(DEFAULT_EXERCISE_NAME)
            .exerciseValue(DEFAULT_EXERCISE_VALUE)
            .exerciseValueType(DEFAULT_EXERCISE_VALUE_TYPE);
        return exerciseValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseValue createUpdatedEntity(EntityManager em) {
        ExerciseValue exerciseValue = new ExerciseValue()
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);
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
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);
        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseValue.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testExerciseValue.getExerciseUuid()).isEqualTo(DEFAULT_EXERCISE_UUID);
        assertThat(testExerciseValue.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testExerciseValue.getExerciseValue()).isEqualTo(DEFAULT_EXERCISE_VALUE);
        assertThat(testExerciseValue.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void createExerciseValueWithExistingId() throws Exception {
        // Create the ExerciseValue with an existing ID
        exerciseValue.setId(1L);
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        int databaseSizeBeforeCreate = exerciseValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
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
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
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
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setExerciseUuid(null);

        // Create the ExerciseValue, which fails.
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setExerciseName(null);

        // Create the ExerciseValue, which fails.
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setExerciseValue(null);

        // Create the ExerciseValue, which fails.
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseValueRepository.findAll().size();
        // set the field null
        exerciseValue.setExerciseValueType(null);

        // Create the ExerciseValue, which fails.
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        restExerciseValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));
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
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.exerciseUuid").value(DEFAULT_EXERCISE_UUID.toString()))
            .andExpect(jsonPath("$.exerciseName").value(DEFAULT_EXERCISE_NAME))
            .andExpect(jsonPath("$.exerciseValue").value(DEFAULT_EXERCISE_VALUE))
            .andExpect(jsonPath("$.exerciseValueType").value(DEFAULT_EXERCISE_VALUE_TYPE.toString()));
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

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseUuid equals to DEFAULT_EXERCISE_UUID
        defaultExerciseValueShouldBeFound("exerciseUuid.equals=" + DEFAULT_EXERCISE_UUID);

        // Get all the exerciseValueList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultExerciseValueShouldNotBeFound("exerciseUuid.equals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseUuid not equals to DEFAULT_EXERCISE_UUID
        defaultExerciseValueShouldNotBeFound("exerciseUuid.notEquals=" + DEFAULT_EXERCISE_UUID);

        // Get all the exerciseValueList where exerciseUuid not equals to UPDATED_EXERCISE_UUID
        defaultExerciseValueShouldBeFound("exerciseUuid.notEquals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseUuidIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseUuid in DEFAULT_EXERCISE_UUID or UPDATED_EXERCISE_UUID
        defaultExerciseValueShouldBeFound("exerciseUuid.in=" + DEFAULT_EXERCISE_UUID + "," + UPDATED_EXERCISE_UUID);

        // Get all the exerciseValueList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultExerciseValueShouldNotBeFound("exerciseUuid.in=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseUuid is not null
        defaultExerciseValueShouldBeFound("exerciseUuid.specified=true");

        // Get all the exerciseValueList where exerciseUuid is null
        defaultExerciseValueShouldNotBeFound("exerciseUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName equals to DEFAULT_EXERCISE_NAME
        defaultExerciseValueShouldBeFound("exerciseName.equals=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseValueList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldNotBeFound("exerciseName.equals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName not equals to DEFAULT_EXERCISE_NAME
        defaultExerciseValueShouldNotBeFound("exerciseName.notEquals=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseValueList where exerciseName not equals to UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldBeFound("exerciseName.notEquals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName in DEFAULT_EXERCISE_NAME or UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldBeFound("exerciseName.in=" + DEFAULT_EXERCISE_NAME + "," + UPDATED_EXERCISE_NAME);

        // Get all the exerciseValueList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldNotBeFound("exerciseName.in=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName is not null
        defaultExerciseValueShouldBeFound("exerciseName.specified=true");

        // Get all the exerciseValueList where exerciseName is null
        defaultExerciseValueShouldNotBeFound("exerciseName.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameContainsSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName contains DEFAULT_EXERCISE_NAME
        defaultExerciseValueShouldBeFound("exerciseName.contains=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseValueList where exerciseName contains UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldNotBeFound("exerciseName.contains=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseNameNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseName does not contain DEFAULT_EXERCISE_NAME
        defaultExerciseValueShouldNotBeFound("exerciseName.doesNotContain=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseValueList where exerciseName does not contain UPDATED_EXERCISE_NAME
        defaultExerciseValueShouldBeFound("exerciseName.doesNotContain=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue equals to DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.equals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.equals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue not equals to DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.notEquals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue not equals to UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.notEquals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue in DEFAULT_EXERCISE_VALUE or UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.in=" + DEFAULT_EXERCISE_VALUE + "," + UPDATED_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.in=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue is not null
        defaultExerciseValueShouldBeFound("exerciseValue.specified=true");

        // Get all the exerciseValueList where exerciseValue is null
        defaultExerciseValueShouldNotBeFound("exerciseValue.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue is greater than or equal to DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.greaterThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue is greater than or equal to UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.greaterThanOrEqual=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue is less than or equal to DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.lessThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue is less than or equal to SMALLER_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.lessThanOrEqual=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue is less than DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.lessThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue is less than UPDATED_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.lessThan=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValue is greater than DEFAULT_EXERCISE_VALUE
        defaultExerciseValueShouldNotBeFound("exerciseValue.greaterThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the exerciseValueList where exerciseValue is greater than SMALLER_EXERCISE_VALUE
        defaultExerciseValueShouldBeFound("exerciseValue.greaterThan=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValueType equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldBeFound("exerciseValueType.equals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the exerciseValueList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldNotBeFound("exerciseValueType.equals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValueType not equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldNotBeFound("exerciseValueType.notEquals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the exerciseValueList where exerciseValueType not equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldBeFound("exerciseValueType.notEquals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValueType in DEFAULT_EXERCISE_VALUE_TYPE or UPDATED_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldBeFound("exerciseValueType.in=" + DEFAULT_EXERCISE_VALUE_TYPE + "," + UPDATED_EXERCISE_VALUE_TYPE);

        // Get all the exerciseValueList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultExerciseValueShouldNotBeFound("exerciseValueType.in=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseValuesByExerciseValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);

        // Get all the exerciseValueList where exerciseValueType is not null
        defaultExerciseValueShouldBeFound("exerciseValueType.specified=true");

        // Get all the exerciseValueList where exerciseValueType is null
        defaultExerciseValueShouldNotBeFound("exerciseValueType.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseValuesByCalendarIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseValueRepository.saveAndFlush(exerciseValue);
        Calendar calendar = CalendarResourceIT.createEntity(em);
        em.persist(calendar);
        em.flush();
        exerciseValue.setCalendar(calendar);
        exerciseValueRepository.saveAndFlush(exerciseValue);
        Long calendarId = calendar.getId();

        // Get all the exerciseValueList where calendar equals to calendarId
        defaultExerciseValueShouldBeFound("calendarId.equals=" + calendarId);

        // Get all the exerciseValueList where calendar equals to (calendarId + 1)
        defaultExerciseValueShouldNotBeFound("calendarId.equals=" + (calendarId + 1));
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
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));

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
        updatedExerciseValue
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(updatedExerciseValue);

        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseValue in the database
        List<ExerciseValue> exerciseValueList = exerciseValueRepository.findAll();
        assertThat(exerciseValueList).hasSize(databaseSizeBeforeUpdate);
        ExerciseValue testExerciseValue = exerciseValueList.get(exerciseValueList.size() - 1);
        assertThat(testExerciseValue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExerciseValue.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testExerciseValue.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testExerciseValue.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testExerciseValue.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
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

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
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

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
            )
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

        partialUpdatedExerciseValue.uuid(UPDATED_UUID).date(UPDATED_DATE).exerciseUuid(UPDATED_EXERCISE_UUID);

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
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExerciseValue.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testExerciseValue.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testExerciseValue.getExerciseValue()).isEqualTo(DEFAULT_EXERCISE_VALUE);
        assertThat(testExerciseValue.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
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

        partialUpdatedExerciseValue
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);

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
        assertThat(testExerciseValue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExerciseValue.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testExerciseValue.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testExerciseValue.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testExerciseValue.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseValue() throws Exception {
        int databaseSizeBeforeUpdate = exerciseValueRepository.findAll().size();
        exerciseValue.setId(count.incrementAndGet());

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
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

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
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

        // Create the ExerciseValue
        ExerciseValueDTO exerciseValueDTO = exerciseValueMapper.toDto(exerciseValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseValueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseValueDTO))
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
