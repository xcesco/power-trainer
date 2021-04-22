package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStepType;
import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutStepCriteria;
import com.abubusoft.powertrainer.service.dto.WorkoutStepDTO;
import com.abubusoft.powertrainer.service.mapper.WorkoutStepMapper;
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
 * Integration tests for the {@link WorkoutStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutStepResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Integer DEFAULT_EXECUTION_TIME = 1;
    private static final Integer UPDATED_EXECUTION_TIME = 2;
    private static final Integer SMALLER_EXECUTION_TIME = 1 - 1;

    private static final WorkoutStepType DEFAULT_TYPE = WorkoutStepType.PREPARE_TIME;
    private static final WorkoutStepType UPDATED_TYPE = WorkoutStepType.COOL_DOWN_TIME;

    private static final WorkoutStatus DEFAULT_STATUS = WorkoutStatus.SCHEDULED;
    private static final WorkoutStatus UPDATED_STATUS = WorkoutStatus.CANCELLED;

    private static final UUID DEFAULT_EXERCISE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_EXERCISE_UUID = UUID.randomUUID();

    private static final String DEFAULT_EXERCISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXERCISE_VALUE = 1;
    private static final Integer UPDATED_EXERCISE_VALUE = 2;
    private static final Integer SMALLER_EXERCISE_VALUE = 1 - 1;

    private static final ValueType DEFAULT_EXERCISE_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_EXERCISE_VALUE_TYPE = ValueType.WEIGHT;

    private static final String ENTITY_API_URL = "/api/workout-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutStepRepository workoutStepRepository;

    @Autowired
    private WorkoutStepMapper workoutStepMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutStepMockMvc;

    private WorkoutStep workoutStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutStep createEntity(EntityManager em) {
        WorkoutStep workoutStep = new WorkoutStep()
            .uuid(DEFAULT_UUID)
            .order(DEFAULT_ORDER)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .exerciseUuid(DEFAULT_EXERCISE_UUID)
            .exerciseName(DEFAULT_EXERCISE_NAME)
            .exerciseValue(DEFAULT_EXERCISE_VALUE)
            .exerciseValueType(DEFAULT_EXERCISE_VALUE_TYPE);
        return workoutStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutStep createUpdatedEntity(EntityManager em) {
        WorkoutStep workoutStep = new WorkoutStep()
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);
        return workoutStep;
    }

    @BeforeEach
    public void initTest() {
        workoutStep = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutStep() throws Exception {
        int databaseSizeBeforeCreate = workoutStepRepository.findAll().size();
        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);
        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWorkoutStep.getExerciseUuid()).isEqualTo(DEFAULT_EXERCISE_UUID);
        assertThat(testWorkoutStep.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testWorkoutStep.getExerciseValue()).isEqualTo(DEFAULT_EXERCISE_VALUE);
        assertThat(testWorkoutStep.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void createWorkoutStepWithExistingId() throws Exception {
        // Create the WorkoutStep with an existing ID
        workoutStep.setId(1L);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        int databaseSizeBeforeCreate = workoutStepRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setUuid(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setExerciseUuid(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setExerciseName(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setExerciseValue(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExerciseValueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutStepRepository.findAll().size();
        // set the field null
        workoutStep.setExerciseValueType(null);

        // Create the WorkoutStep, which fails.
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        restWorkoutStepMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkoutSteps() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get the workoutStep
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutStep.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.exerciseUuid").value(DEFAULT_EXERCISE_UUID.toString()))
            .andExpect(jsonPath("$.exerciseName").value(DEFAULT_EXERCISE_NAME))
            .andExpect(jsonPath("$.exerciseValue").value(DEFAULT_EXERCISE_VALUE))
            .andExpect(jsonPath("$.exerciseValueType").value(DEFAULT_EXERCISE_VALUE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getWorkoutStepsByIdFiltering() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        Long id = workoutStep.getId();

        defaultWorkoutStepShouldBeFound("id.equals=" + id);
        defaultWorkoutStepShouldNotBeFound("id.notEquals=" + id);

        defaultWorkoutStepShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkoutStepShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkoutStepShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkoutStepShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where uuid equals to DEFAULT_UUID
        defaultWorkoutStepShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the workoutStepList where uuid equals to UPDATED_UUID
        defaultWorkoutStepShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where uuid not equals to DEFAULT_UUID
        defaultWorkoutStepShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the workoutStepList where uuid not equals to UPDATED_UUID
        defaultWorkoutStepShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultWorkoutStepShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the workoutStepList where uuid equals to UPDATED_UUID
        defaultWorkoutStepShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where uuid is not null
        defaultWorkoutStepShouldBeFound("uuid.specified=true");

        // Get all the workoutStepList where uuid is null
        defaultWorkoutStepShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order equals to DEFAULT_ORDER
        defaultWorkoutStepShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order equals to UPDATED_ORDER
        defaultWorkoutStepShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order not equals to DEFAULT_ORDER
        defaultWorkoutStepShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order not equals to UPDATED_ORDER
        defaultWorkoutStepShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultWorkoutStepShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the workoutStepList where order equals to UPDATED_ORDER
        defaultWorkoutStepShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order is not null
        defaultWorkoutStepShouldBeFound("order.specified=true");

        // Get all the workoutStepList where order is null
        defaultWorkoutStepShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order is greater than or equal to DEFAULT_ORDER
        defaultWorkoutStepShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order is greater than or equal to UPDATED_ORDER
        defaultWorkoutStepShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order is less than or equal to DEFAULT_ORDER
        defaultWorkoutStepShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order is less than or equal to SMALLER_ORDER
        defaultWorkoutStepShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order is less than DEFAULT_ORDER
        defaultWorkoutStepShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order is less than UPDATED_ORDER
        defaultWorkoutStepShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where order is greater than DEFAULT_ORDER
        defaultWorkoutStepShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the workoutStepList where order is greater than SMALLER_ORDER
        defaultWorkoutStepShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime equals to DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.equals=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime equals to UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.equals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime not equals to DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.notEquals=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime not equals to UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.notEquals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime in DEFAULT_EXECUTION_TIME or UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.in=" + DEFAULT_EXECUTION_TIME + "," + UPDATED_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime equals to UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.in=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime is not null
        defaultWorkoutStepShouldBeFound("executionTime.specified=true");

        // Get all the workoutStepList where executionTime is null
        defaultWorkoutStepShouldNotBeFound("executionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime is greater than or equal to DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.greaterThanOrEqual=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime is greater than or equal to UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.greaterThanOrEqual=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime is less than or equal to DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.lessThanOrEqual=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime is less than or equal to SMALLER_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.lessThanOrEqual=" + SMALLER_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime is less than DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.lessThan=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime is less than UPDATED_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.lessThan=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExecutionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where executionTime is greater than DEFAULT_EXECUTION_TIME
        defaultWorkoutStepShouldNotBeFound("executionTime.greaterThan=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutStepList where executionTime is greater than SMALLER_EXECUTION_TIME
        defaultWorkoutStepShouldBeFound("executionTime.greaterThan=" + SMALLER_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where type equals to DEFAULT_TYPE
        defaultWorkoutStepShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the workoutStepList where type equals to UPDATED_TYPE
        defaultWorkoutStepShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where type not equals to DEFAULT_TYPE
        defaultWorkoutStepShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the workoutStepList where type not equals to UPDATED_TYPE
        defaultWorkoutStepShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultWorkoutStepShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the workoutStepList where type equals to UPDATED_TYPE
        defaultWorkoutStepShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where type is not null
        defaultWorkoutStepShouldBeFound("type.specified=true");

        // Get all the workoutStepList where type is null
        defaultWorkoutStepShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where status equals to DEFAULT_STATUS
        defaultWorkoutStepShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the workoutStepList where status equals to UPDATED_STATUS
        defaultWorkoutStepShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where status not equals to DEFAULT_STATUS
        defaultWorkoutStepShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the workoutStepList where status not equals to UPDATED_STATUS
        defaultWorkoutStepShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWorkoutStepShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the workoutStepList where status equals to UPDATED_STATUS
        defaultWorkoutStepShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where status is not null
        defaultWorkoutStepShouldBeFound("status.specified=true");

        // Get all the workoutStepList where status is null
        defaultWorkoutStepShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseUuid equals to DEFAULT_EXERCISE_UUID
        defaultWorkoutStepShouldBeFound("exerciseUuid.equals=" + DEFAULT_EXERCISE_UUID);

        // Get all the workoutStepList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultWorkoutStepShouldNotBeFound("exerciseUuid.equals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseUuid not equals to DEFAULT_EXERCISE_UUID
        defaultWorkoutStepShouldNotBeFound("exerciseUuid.notEquals=" + DEFAULT_EXERCISE_UUID);

        // Get all the workoutStepList where exerciseUuid not equals to UPDATED_EXERCISE_UUID
        defaultWorkoutStepShouldBeFound("exerciseUuid.notEquals=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseUuid in DEFAULT_EXERCISE_UUID or UPDATED_EXERCISE_UUID
        defaultWorkoutStepShouldBeFound("exerciseUuid.in=" + DEFAULT_EXERCISE_UUID + "," + UPDATED_EXERCISE_UUID);

        // Get all the workoutStepList where exerciseUuid equals to UPDATED_EXERCISE_UUID
        defaultWorkoutStepShouldNotBeFound("exerciseUuid.in=" + UPDATED_EXERCISE_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseUuid is not null
        defaultWorkoutStepShouldBeFound("exerciseUuid.specified=true");

        // Get all the workoutStepList where exerciseUuid is null
        defaultWorkoutStepShouldNotBeFound("exerciseUuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName equals to DEFAULT_EXERCISE_NAME
        defaultWorkoutStepShouldBeFound("exerciseName.equals=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutStepList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldNotBeFound("exerciseName.equals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName not equals to DEFAULT_EXERCISE_NAME
        defaultWorkoutStepShouldNotBeFound("exerciseName.notEquals=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutStepList where exerciseName not equals to UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldBeFound("exerciseName.notEquals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName in DEFAULT_EXERCISE_NAME or UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldBeFound("exerciseName.in=" + DEFAULT_EXERCISE_NAME + "," + UPDATED_EXERCISE_NAME);

        // Get all the workoutStepList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldNotBeFound("exerciseName.in=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName is not null
        defaultWorkoutStepShouldBeFound("exerciseName.specified=true");

        // Get all the workoutStepList where exerciseName is null
        defaultWorkoutStepShouldNotBeFound("exerciseName.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameContainsSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName contains DEFAULT_EXERCISE_NAME
        defaultWorkoutStepShouldBeFound("exerciseName.contains=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutStepList where exerciseName contains UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldNotBeFound("exerciseName.contains=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseNameNotContainsSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseName does not contain DEFAULT_EXERCISE_NAME
        defaultWorkoutStepShouldNotBeFound("exerciseName.doesNotContain=" + DEFAULT_EXERCISE_NAME);

        // Get all the workoutStepList where exerciseName does not contain UPDATED_EXERCISE_NAME
        defaultWorkoutStepShouldBeFound("exerciseName.doesNotContain=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue equals to DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.equals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.equals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue not equals to DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.notEquals=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue not equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.notEquals=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue in DEFAULT_EXERCISE_VALUE or UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.in=" + DEFAULT_EXERCISE_VALUE + "," + UPDATED_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue equals to UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.in=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue is not null
        defaultWorkoutStepShouldBeFound("exerciseValue.specified=true");

        // Get all the workoutStepList where exerciseValue is null
        defaultWorkoutStepShouldNotBeFound("exerciseValue.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue is greater than or equal to DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.greaterThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue is greater than or equal to UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.greaterThanOrEqual=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue is less than or equal to DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.lessThanOrEqual=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue is less than or equal to SMALLER_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.lessThanOrEqual=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue is less than DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.lessThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue is less than UPDATED_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.lessThan=" + UPDATED_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValue is greater than DEFAULT_EXERCISE_VALUE
        defaultWorkoutStepShouldNotBeFound("exerciseValue.greaterThan=" + DEFAULT_EXERCISE_VALUE);

        // Get all the workoutStepList where exerciseValue is greater than SMALLER_EXERCISE_VALUE
        defaultWorkoutStepShouldBeFound("exerciseValue.greaterThan=" + SMALLER_EXERCISE_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValueType equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("exerciseValueType.equals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the workoutStepList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("exerciseValueType.equals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValueType not equals to DEFAULT_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("exerciseValueType.notEquals=" + DEFAULT_EXERCISE_VALUE_TYPE);

        // Get all the workoutStepList where exerciseValueType not equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("exerciseValueType.notEquals=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValueType in DEFAULT_EXERCISE_VALUE_TYPE or UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("exerciseValueType.in=" + DEFAULT_EXERCISE_VALUE_TYPE + "," + UPDATED_EXERCISE_VALUE_TYPE);

        // Get all the workoutStepList where exerciseValueType equals to UPDATED_EXERCISE_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("exerciseValueType.in=" + UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByExerciseValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where exerciseValueType is not null
        defaultWorkoutStepShouldBeFound("exerciseValueType.specified=true");

        // Get all the workoutStepList where exerciseValueType is null
        defaultWorkoutStepShouldNotBeFound("exerciseValueType.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByWorkoutIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);
        Workout workout = WorkoutResourceIT.createEntity(em);
        em.persist(workout);
        em.flush();
        workoutStep.setWorkout(workout);
        workoutStepRepository.saveAndFlush(workoutStep);
        Long workoutId = workout.getId();

        // Get all the workoutStepList where workout equals to workoutId
        defaultWorkoutStepShouldBeFound("workoutId.equals=" + workoutId);

        // Get all the workoutStepList where workout equals to (workoutId + 1)
        defaultWorkoutStepShouldNotBeFound("workoutId.equals=" + (workoutId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkoutStepShouldBeFound(String filter) throws Exception {
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].exerciseUuid").value(hasItem(DEFAULT_EXERCISE_UUID.toString())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].exerciseValue").value(hasItem(DEFAULT_EXERCISE_VALUE)))
            .andExpect(jsonPath("$.[*].exerciseValueType").value(hasItem(DEFAULT_EXERCISE_VALUE_TYPE.toString())));

        // Check, that the count call also returns 1
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkoutStepShouldNotBeFound(String filter) throws Exception {
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkoutStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutStep() throws Exception {
        // Get the workoutStep
        restWorkoutStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep
        WorkoutStep updatedWorkoutStep = workoutStepRepository.findById(workoutStep.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutStep are not directly saved in db
        em.detach(updatedWorkoutStep);
        updatedWorkoutStep
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(updatedWorkoutStep);

        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkoutStep.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testWorkoutStep.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testWorkoutStep.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testWorkoutStep.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep.type(UPDATED_TYPE).status(UPDATED_STATUS).exerciseUuid(UPDATED_EXERCISE_UUID);

        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkoutStep.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testWorkoutStep.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testWorkoutStep.getExerciseValue()).isEqualTo(DEFAULT_EXERCISE_VALUE);
        assertThat(testWorkoutStep.getExerciseValueType()).isEqualTo(DEFAULT_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutStepWithPatch() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();

        // Update the workoutStep using partial update
        WorkoutStep partialUpdatedWorkoutStep = new WorkoutStep();
        partialUpdatedWorkoutStep.setId(workoutStep.getId());

        partialUpdatedWorkoutStep
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .exerciseUuid(UPDATED_EXERCISE_UUID)
            .exerciseName(UPDATED_EXERCISE_NAME)
            .exerciseValue(UPDATED_EXERCISE_VALUE)
            .exerciseValueType(UPDATED_EXERCISE_VALUE_TYPE);

        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkoutStep.getExerciseUuid()).isEqualTo(UPDATED_EXERCISE_UUID);
        assertThat(testWorkoutStep.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testWorkoutStep.getExerciseValue()).isEqualTo(UPDATED_EXERCISE_VALUE);
        assertThat(testWorkoutStep.getExerciseValueType()).isEqualTo(UPDATED_EXERCISE_VALUE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutStepDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // Create the WorkoutStep
        WorkoutStepDTO workoutStepDTO = workoutStepMapper.toDto(workoutStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workoutStepDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkoutStep() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        int databaseSizeBeforeDelete = workoutStepRepository.findAll().size();

        // Delete the workoutStep
        restWorkoutStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
