package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.WorkoutSheet;
import com.abubusoft.powertrainer.domain.WorkoutSheetExercise;
import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.repository.WorkoutSheetExerciseRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetExerciseCriteria;
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
 * Integration tests for the {@link WorkoutSheetExerciseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutSheetExerciseResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;
    private static final Integer SMALLER_REPETITIONS = 1 - 1;

    private static final UUID DEFAULT_EXERCISE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_EXERCISE_UUID = UUID.randomUUID();

    private static final String DEFAULT_EXERCISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXERCISE_VALUE = 1;
    private static final Integer UPDATED_EXERCISE_VALUE = 2;
    private static final Integer SMALLER_EXERCISE_VALUE = 1 - 1;

    private static final ValueType DEFAULT_EXERCISE_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_EXERCISE_VALUE_TYPE = ValueType.WEIGHT;

    private static final String ENTITY_API_URL = "/api/workout-sheet-exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutSheetExerciseRepository workoutSheetExerciseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutSheetExerciseMockMvc;

    private WorkoutSheetExercise workoutSheetExercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutSheetExercise createEntity(EntityManager em) {
        WorkoutSheetExercise workoutSheetExercise = new WorkoutSheetExercise()
            .uuid(DEFAULT_UUID)
            .order(DEFAULT_ORDER)
            .repetitions(DEFAULT_REPETITIONS)
            .exerciseUuid(DEFAULT_EXERCISE_UUID)
            .exerciseName(DEFAULT_EXERCISE_NAME)
            .exerciseValue(DEFAULT_EXERCISE_VALUE)
            .exerciseValueType(DEFAULT_EXERCISE_VALUE_TYPE);
        return workoutSheetExercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutSheetExercise createUpdatedEntity(EntityManager em) {
        WorkoutSheetExercise workoutSheetExercise = new WorkoutSheetExercise()
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .repetitions(UPDATED_REPETITIONS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);
        return workoutSheetExercise;
    }

    @BeforeEach
    public void initTest() {
        workoutSheetExercise = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeCreate = workoutSheetExerciseRepository.findAll().size();
        // Create the WorkoutSheetExercise
        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutSheetExercise testWorkoutSheetExercise = workoutSheetExerciseList.get(workoutSheetExerciseList.size() - 1);
        assertThat(testWorkoutSheetExercise.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkoutSheetExercise.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testWorkoutSheetExercise.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
        assertThat(testWorkoutSheetExercise.getExerciseUuid()).isEqualTo(DEFAULT_EXERCISE_UUID);
        assertThat(testWorkoutSheetExercise.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testWorkoutSheetExercise.getExerciseValue()).isEqualTo(DEFAULT_EXERCISE_VALUE);
        assertThat(testWorkoutSheetExercise.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void createWorkoutSheetExerciseWithExistingId() throws Exception {
        // Create the WorkoutSheetExercise with an existing ID
        workoutSheetExercise.setId(1L);

        int databaseSizeBeforeCreate = workoutSheetExerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetExerciseRepository.findAll().size();
        // set the field null
        workoutSheetExercise.setUuid(null);

        // Create the WorkoutSheetExercise, which fails.

        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetExerciseRepository.findAll().size();
        // set the field null
        workoutSheetExercise.setExerciseUuid(null);

        // Create the WorkoutSheetExercise, which fails.

        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetExerciseRepository.findAll().size();
        // set the field null
        workoutSheetExercise.setExerciseName(null);

        // Create the WorkoutSheetExercise, which fails.

        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetExerciseRepository.findAll().size();
        // set the field null
        workoutSheetExercise.setExerciseValue(null);

        // Create the WorkoutSheetExercise, which fails.

        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetExerciseRepository.findAll().size();
        // set the field null
        workoutSheetExercise.setExerciseValueType(null);

        // Create the WorkoutSheetExercise, which fails.

        restWorkoutSheetExerciseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercises() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutSheetExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getWorkoutSheetExercise() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get the workoutSheetExercise
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutSheetExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutSheetExercise.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.repetitions").value(DEFAULT_REPETITIONS))
            .andExpect(jsonPath("$.exerciseUuid").value(DEFAULT_EXERCISE_UUID.toString()))
            .andExpect(jsonPath("$.exerciseName").value(DEFAULT_EXERCISE_NAME))
            .andExpect(jsonPath("$.exerciseValue").value(DEFAULT_EXERCISE_VALUE))
            .andExpect(jsonPath("$.exerciseValueType").value(DEFAULT_EXERCISE_VALUE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getWorkoutSheetExercisesByIdFiltering() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        Long id = workoutSheetExercise.getId();

        defaultWorkoutSheetExerciseShouldBeFound("id.equals=" + id);
        defaultWorkoutSheetExerciseShouldNotBeFound("id.notEquals=" + id);

        defaultWorkoutSheetExerciseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkoutSheetExerciseShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkoutSheetExerciseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkoutSheetExerciseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where uuid equals to DEFAULT_UUID
        defaultWorkoutSheetExerciseShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the workoutSheetExerciseList where uuid equals to UPDATED_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where uuid not equals to DEFAULT_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the workoutSheetExerciseList where uuid not equals to UPDATED_UUID
        defaultWorkoutSheetExerciseShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultWorkoutSheetExerciseShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the workoutSheetExerciseList where uuid equals to UPDATED_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where uuid is not null
        defaultWorkoutSheetExerciseShouldBeFound("uuid.specified=true");

        // Get all the workoutSheetExerciseList where uuid is null
        defaultWorkoutSheetExerciseShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order equals to DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order equals to UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order not equals to DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order not equals to UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the workoutSheetExerciseList where order equals to UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order is not null
        defaultWorkoutSheetExerciseShouldBeFound("order.specified=true");

        // Get all the workoutSheetExerciseList where order is null
        defaultWorkoutSheetExerciseShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order is greater than or equal to DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order is greater than or equal to UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order is less than or equal to DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order is less than or equal to SMALLER_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order is less than DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order is less than UPDATED_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where order is greater than DEFAULT_ORDER
        defaultWorkoutSheetExerciseShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the workoutSheetExerciseList where order is greater than SMALLER_ORDER
        defaultWorkoutSheetExerciseShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions equals to DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.equals=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions equals to UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.equals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions not equals to DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.notEquals=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions not equals to UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.notEquals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions in DEFAULT_REPETITIONS or UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.in=" + DEFAULT_REPETITIONS + "," + UPDATED_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions equals to UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.in=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions is not null
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.specified=true");

        // Get all the workoutSheetExerciseList where repetitions is null
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions is greater than or equal to DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.greaterThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions is greater than or equal to UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.greaterThanOrEqual=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions is less than or equal to DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.lessThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions is less than or equal to SMALLER_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.lessThanOrEqual=" + SMALLER_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions is less than DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.lessThan=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions is less than UPDATED_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.lessThan=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetitions is greater than DEFAULT_REPETITIONS
        defaultWorkoutSheetExerciseShouldNotBeFound("repetitions.greaterThan=" + DEFAULT_REPETITIONS);

        // Get all the workoutSheetExerciseList where repetitions is greater than SMALLER_REPETITIONS
        defaultWorkoutSheetExerciseShouldBeFound("repetitions.greaterThan=" + SMALLER_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseUuid equals to DEFAULT_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldBeFound("exerciseUuid.equals=" + DEFAULT_EXERCISE_UUID);

        // Get all the workoutSheetExerciseList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseUuid.equals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseUuid not equals to DEFAULT_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseUuid.notEquals=" + DEFAULT_EXERCISE_UUID);

        // Get all the workoutSheetExerciseList where exerciseUuid not equals to UPDATED_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldBeFound("exerciseUuid.notEquals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseUuid in DEFAULT_EXERCISE_UUID or UPDATED_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldBeFound("exerciseUuid.in=" + DEFAULT_EXERCISE_UUID + "," + UPDATED_EXERCISE_UUID);

        // Get all the workoutSheetExerciseList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseUuid.in=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseUuid is not null
        defaultWorkoutSheetExerciseShouldBeFound("exerciseUuid.specified=true");

        // Get all the workoutSheetExerciseList where exerciseUuid is null
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName equals to DEFAULT_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.equals=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutSheetExerciseList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.equals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName not equals to DEFAULT_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.notEquals=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutSheetExerciseList where exerciseName not equals to UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.notEquals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName in DEFAULT_EXERCISE_NAME or UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.in=" + DEFAULT_EXERCISE_NAME + "," + UPDATED_EXERCISE_NAME);

        // Get all the workoutSheetExerciseList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.in=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName is not null
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.specified=true");

        // Get all the workoutSheetExerciseList where exerciseName is null
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameContainsSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName contains DEFAULT_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.contains=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutSheetExerciseList where exerciseName contains UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.contains=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseNameNotContainsSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseName does not contain DEFAULT_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseName.doesNotContain=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutSheetExerciseList where exerciseName does not contain UPDATED_EXERCISE_NAME
        defaultWorkoutSheetExerciseShouldBeFound("exerciseName.doesNotContain=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue equals to DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.equals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.equals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue not equals to DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.notEquals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue not equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.notEquals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue in DEFAULT_EXERCISE_VALUE or UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.in=" + DEFAULT_EXERCISE_VALUE + "," + UPDATED_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.in=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue is not null
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.specified=true");

        // Get all the workoutSheetExerciseList where exerciseValue is null
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue is greater than or equal to DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.greaterThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue is greater than or equal to UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.greaterThanOrEqual=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue is less than or equal to DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.lessThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue is less than or equal to SMALLER_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.lessThanOrEqual=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue is less than DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.lessThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue is less than UPDATED_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.lessThan=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValue is greater than DEFAULT_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValue.greaterThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutSheetExerciseList where exerciseValue is greater than SMALLER_EXERCISE_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValue.greaterThan=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValueType equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValueType.equals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValueType.equals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValueType not equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValueType.notEquals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where exerciseValueType not equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValueType.notEquals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValueType in DEFAULT_EXERCISE_VALUE_TYPE or UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValueType.in=" + DEFAULT_EXERCISE_VALUE_TYPE + "," + UPDATED_EXERCISE_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValueType.in=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByExerciseValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where exerciseValueType is not null
        defaultWorkoutSheetExerciseShouldBeFound("exerciseValueType.specified=true");

        // Get all the workoutSheetExerciseList where exerciseValueType is null
        defaultWorkoutSheetExerciseShouldNotBeFound("exerciseValueType.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByWorkoutSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);
        WorkoutSheet workoutSheet = WorkoutSheetResourceIT.createEntity(em);
        em.persist(workoutSheet);
        em.flush();
        workoutSheetExercise.setWorkoutSheet(workoutSheet);
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);
        Long workoutSheetId = workoutSheet.getId();

        // Get all the workoutSheetExerciseList where workoutSheet equals to workoutSheetId
        defaultWorkoutSheetExerciseShouldBeFound("workoutSheetId.equals=" + workoutSheetId);

        // Get all the workoutSheetExerciseList where workoutSheet equals to (workoutSheetId + 1)
        defaultWorkoutSheetExerciseShouldNotBeFound("workoutSheetId.equals=" + (workoutSheetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkoutSheetExerciseShouldBeFound(String filter) throws Exception {
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutSheetExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));

        // Check, that the count call also returns 1
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkoutSheetExerciseShouldNotBeFound(String filter) throws Exception {
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkoutSheetExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutSheetExercise() throws Exception {
        // Get the workoutSheetExercise
        restWorkoutSheetExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutSheetExercise() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();

        // Update the workoutSheetExercise
        WorkoutSheetExercise updatedWorkoutSheetExercise = workoutSheetExerciseRepository.findById(workoutSheetExercise.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutSheetExercise are not directly saved in db
        em.detach(updatedWorkoutSheetExercise);
        updatedWorkoutSheetExercise
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .repetitions(UPDATED_REPETITIONS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);

        restWorkoutSheetExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutSheetExercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutSheetExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheetExercise testWorkoutSheetExercise = workoutSheetExerciseList.get(workoutSheetExerciseList.size() - 1);
        assertThat(testWorkoutSheetExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheetExercise.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutSheetExercise.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testWorkoutSheetExercise.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testWorkoutSheetExercise.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testWorkoutSheetExercise.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testWorkoutSheetExercise.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutSheetExercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutSheetExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();

        // Update the workoutSheetExercise using partial update
        WorkoutSheetExercise partialUpdatedWorkoutSheetExercise = new WorkoutSheetExercise();
        partialUpdatedWorkoutSheetExercise.setId(workoutSheetExercise.getId());

        partialUpdatedWorkoutSheetExercise
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .repetitions(UPDATED_REPETITIONS)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE);

        restWorkoutSheetExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutSheetExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutSheetExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheetExercise testWorkoutSheetExercise = workoutSheetExerciseList.get(workoutSheetExerciseList.size() - 1);
        assertThat(testWorkoutSheetExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheetExercise.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutSheetExercise.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testWorkoutSheetExercise.getExerciseUuid()).isEqualTo(DEFAULT_EXERCISE_UUID);
        assertThat(testWorkoutSheetExercise.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testWorkoutSheetExercise.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testWorkoutSheetExercise.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutSheetExerciseWithPatch() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();

        // Update the workoutSheetExercise using partial update
        WorkoutSheetExercise partialUpdatedWorkoutSheetExercise = new WorkoutSheetExercise();
        partialUpdatedWorkoutSheetExercise.setId(workoutSheetExercise.getId());

        partialUpdatedWorkoutSheetExercise
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .repetitions(UPDATED_REPETITIONS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);

        restWorkoutSheetExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutSheetExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutSheetExercise))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheetExercise testWorkoutSheetExercise = workoutSheetExerciseList.get(workoutSheetExerciseList.size() - 1);
        assertThat(testWorkoutSheetExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheetExercise.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutSheetExercise.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testWorkoutSheetExercise.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testWorkoutSheetExercise.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testWorkoutSheetExercise.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testWorkoutSheetExercise.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutSheetExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutSheetExercise() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetExerciseRepository.findAll().size();
        workoutSheetExercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheetExercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutSheetExercise in the database
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkoutSheetExercise() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        int databaseSizeBeforeDelete = workoutSheetExerciseRepository.findAll().size();

        // Delete the workoutSheetExercise
        restWorkoutSheetExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutSheetExercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutSheetExercise> workoutSheetExerciseList = workoutSheetExerciseRepository.findAll();
        assertThat(workoutSheetExerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
