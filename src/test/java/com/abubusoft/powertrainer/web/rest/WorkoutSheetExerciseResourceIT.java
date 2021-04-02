package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
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

    private static final Integer DEFAULT_REPETITION = 1;
    private static final Integer UPDATED_REPETITION = 2;
    private static final Integer SMALLER_REPETITION = 1 - 1;

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final Integer SMALLER_VALUE = 1 - 1;

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.WEIGHT;

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
            .repetition(DEFAULT_REPETITION)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE);
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
            .repetition(UPDATED_REPETITION)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);
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
        assertThat(testWorkoutSheetExercise.getRepetition()).isEqualTo(DEFAULT_REPETITION);
        assertThat(testWorkoutSheetExercise.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWorkoutSheetExercise.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
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
            .andExpect(jsonPath("$.[*].repetition").value(hasItem(DEFAULT_REPETITION)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())));
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
            .andExpect(jsonPath("$.repetition").value(DEFAULT_REPETITION))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()));
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
    void getAllWorkoutSheetExercisesByRepetitionIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition equals to DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.equals=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition equals to UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.equals=" + UPDATED_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition not equals to DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.notEquals=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition not equals to UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.notEquals=" + UPDATED_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition in DEFAULT_REPETITION or UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.in=" + DEFAULT_REPETITION + "," + UPDATED_REPETITION);

        // Get all the workoutSheetExerciseList where repetition equals to UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.in=" + UPDATED_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition is not null
        defaultWorkoutSheetExerciseShouldBeFound("repetition.specified=true");

        // Get all the workoutSheetExerciseList where repetition is null
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition is greater than or equal to DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.greaterThanOrEqual=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition is greater than or equal to UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.greaterThanOrEqual=" + UPDATED_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition is less than or equal to DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.lessThanOrEqual=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition is less than or equal to SMALLER_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.lessThanOrEqual=" + SMALLER_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition is less than DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.lessThan=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition is less than UPDATED_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.lessThan=" + UPDATED_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByRepetitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where repetition is greater than DEFAULT_REPETITION
        defaultWorkoutSheetExerciseShouldNotBeFound("repetition.greaterThan=" + DEFAULT_REPETITION);

        // Get all the workoutSheetExerciseList where repetition is greater than SMALLER_REPETITION
        defaultWorkoutSheetExerciseShouldBeFound("repetition.greaterThan=" + SMALLER_REPETITION);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value equals to DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value equals to UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value not equals to DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value not equals to UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the workoutSheetExerciseList where value equals to UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value is not null
        defaultWorkoutSheetExerciseShouldBeFound("value.specified=true");

        // Get all the workoutSheetExerciseList where value is null
        defaultWorkoutSheetExerciseShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value is greater than or equal to DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value is greater than or equal to UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value is less than or equal to DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value is less than or equal to SMALLER_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value is less than DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value is less than UPDATED_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where value is greater than DEFAULT_VALUE
        defaultWorkoutSheetExerciseShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the workoutSheetExerciseList where value is greater than SMALLER_VALUE
        defaultWorkoutSheetExerciseShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where valueType equals to DEFAULT_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where valueType equals to UPDATED_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where valueType not equals to DEFAULT_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("valueType.notEquals=" + DEFAULT_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where valueType not equals to UPDATED_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("valueType.notEquals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the workoutSheetExerciseList where valueType equals to UPDATED_VALUE_TYPE
        defaultWorkoutSheetExerciseShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetExercisesByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetExerciseRepository.saveAndFlush(workoutSheetExercise);

        // Get all the workoutSheetExerciseList where valueType is not null
        defaultWorkoutSheetExerciseShouldBeFound("valueType.specified=true");

        // Get all the workoutSheetExerciseList where valueType is null
        defaultWorkoutSheetExerciseShouldNotBeFound("valueType.specified=false");
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
            .andExpect(jsonPath("$.[*].repetition").value(hasItem(DEFAULT_REPETITION)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())));

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
            .repetition(UPDATED_REPETITION)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);

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
        assertThat(testWorkoutSheetExercise.getRepetition()).isEqualTo(UPDATED_REPETITION);
        assertThat(testWorkoutSheetExercise.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWorkoutSheetExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
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
            .repetition(UPDATED_REPETITION)
            .valueType(UPDATED_VALUE_TYPE);

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
        assertThat(testWorkoutSheetExercise.getRepetition()).isEqualTo(UPDATED_REPETITION);
        assertThat(testWorkoutSheetExercise.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWorkoutSheetExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
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
            .repetition(UPDATED_REPETITION)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);

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
        assertThat(testWorkoutSheetExercise.getRepetition()).isEqualTo(UPDATED_REPETITION);
        assertThat(testWorkoutSheetExercise.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWorkoutSheetExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
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
