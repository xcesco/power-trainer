package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStepType;
import com.abubusoft.powertrainer.repository.WorkoutStepRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutStepCriteria;
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

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final Integer SMALLER_VALUE = 1 - 1;

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.WEIGHT;

    private static final Integer DEFAULT_EXECUTION_TIME = 1;
    private static final Integer UPDATED_EXECUTION_TIME = 2;
    private static final Integer SMALLER_EXECUTION_TIME = 1 - 1;

    private static final WorkoutStepType DEFAULT_TYPE = WorkoutStepType.PREPARE_TIME;
    private static final WorkoutStepType UPDATED_TYPE = WorkoutStepType.COOL_DOWN_TIME;

    private static final WorkoutStatus DEFAULT_STATUS = WorkoutStatus.SCHEDULED;
    private static final WorkoutStatus UPDATED_STATUS = WorkoutStatus.CANCELLED;

    private static final String ENTITY_API_URL = "/api/workout-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutStepRepository workoutStepRepository;

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
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
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
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
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
        restWorkoutStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStep)))
            .andExpect(status().isCreated());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testWorkoutStep.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWorkoutStep.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWorkoutStepWithExistingId() throws Exception {
        // Create the WorkoutStep with an existing ID
        workoutStep.setId(1L);

        int databaseSizeBeforeCreate = workoutStepRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStep)))
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

        restWorkoutStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStep)))
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
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
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
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
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
    void getAllWorkoutStepsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value equals to DEFAULT_VALUE
        defaultWorkoutStepShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value equals to UPDATED_VALUE
        defaultWorkoutStepShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value not equals to DEFAULT_VALUE
        defaultWorkoutStepShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value not equals to UPDATED_VALUE
        defaultWorkoutStepShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultWorkoutStepShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the workoutStepList where value equals to UPDATED_VALUE
        defaultWorkoutStepShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value is not null
        defaultWorkoutStepShouldBeFound("value.specified=true");

        // Get all the workoutStepList where value is null
        defaultWorkoutStepShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value is greater than or equal to DEFAULT_VALUE
        defaultWorkoutStepShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value is greater than or equal to UPDATED_VALUE
        defaultWorkoutStepShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value is less than or equal to DEFAULT_VALUE
        defaultWorkoutStepShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value is less than or equal to SMALLER_VALUE
        defaultWorkoutStepShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value is less than DEFAULT_VALUE
        defaultWorkoutStepShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value is less than UPDATED_VALUE
        defaultWorkoutStepShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where value is greater than DEFAULT_VALUE
        defaultWorkoutStepShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the workoutStepList where value is greater than SMALLER_VALUE
        defaultWorkoutStepShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where valueType equals to DEFAULT_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the workoutStepList where valueType equals to UPDATED_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where valueType not equals to DEFAULT_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("valueType.notEquals=" + DEFAULT_VALUE_TYPE);

        // Get all the workoutStepList where valueType not equals to UPDATED_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("valueType.notEquals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultWorkoutStepShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the workoutStepList where valueType equals to UPDATED_VALUE_TYPE
        defaultWorkoutStepShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutStepsByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutStepRepository.saveAndFlush(workoutStep);

        // Get all the workoutStepList where valueType is not null
        defaultWorkoutStepShouldBeFound("valueType.specified=true");

        // Get all the workoutStepList where valueType is null
        defaultWorkoutStepShouldNotBeFound("valueType.specified=false");
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
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

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
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);

        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutStep in the database
        List<WorkoutStep> workoutStepList = workoutStepRepository.findAll();
        assertThat(workoutStepList).hasSize(databaseSizeBeforeUpdate);
        WorkoutStep testWorkoutStep = workoutStepList.get(workoutStepList.size() - 1);
        assertThat(testWorkoutStep.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutStep.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkoutStep.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWorkoutStep.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutStep.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStep))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutStep))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutStep)))
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

        partialUpdatedWorkoutStep.valueType(UPDATED_VALUE_TYPE).executionTime(UPDATED_EXECUTION_TIME).type(UPDATED_TYPE);

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
        assertThat(testWorkoutStep.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWorkoutStep.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(DEFAULT_STATUS);
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
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);

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
        assertThat(testWorkoutStep.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWorkoutStep.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testWorkoutStep.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkoutStep.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkoutStep.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutStep() throws Exception {
        int databaseSizeBeforeUpdate = workoutStepRepository.findAll().size();
        workoutStep.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStep))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutStep))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutStepMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workoutStep))
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
