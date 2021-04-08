package com.abubusoft.powertrainer.web.rest;

import static com.abubusoft.powertrainer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.domain.WorkoutStep;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import com.abubusoft.powertrainer.repository.WorkoutRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutCriteria;
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
 * Integration tests for the {@link WorkoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final WorkoutType DEFAULT_TYPE = WorkoutType.SEQUENCE;
    private static final WorkoutType UPDATED_TYPE = WorkoutType.SUPERSET;

    private static final Integer DEFAULT_EXECUTION_TIME = 1;
    private static final Integer UPDATED_EXECUTION_TIME = 2;
    private static final Integer SMALLER_EXECUTION_TIME = 1 - 1;

    private static final Integer DEFAULT_PREVIEW_TIME = 1;
    private static final Integer UPDATED_PREVIEW_TIME = 2;
    private static final Integer SMALLER_PREVIEW_TIME = 1 - 1;

    private static final WorkoutStatus DEFAULT_STATUS = WorkoutStatus.SCHEDULED;
    private static final WorkoutStatus UPDATED_STATUS = WorkoutStatus.CANCELLED;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutMockMvc;

    private Workout workout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createEntity(EntityManager em) {
        Workout workout = new Workout()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .type(DEFAULT_TYPE)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .previewTime(DEFAULT_PREVIEW_TIME)
            .status(DEFAULT_STATUS)
            .date(DEFAULT_DATE)
            .note(DEFAULT_NOTE);
        return workout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workout createUpdatedEntity(EntityManager em) {
        Workout workout = new Workout()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .type(UPDATED_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .previewTime(UPDATED_PREVIEW_TIME)
            .status(UPDATED_STATUS)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE);
        return workout;
    }

    @BeforeEach
    public void initTest() {
        workout = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkout() throws Exception {
        int databaseSizeBeforeCreate = workoutRepository.findAll().size();
        // Create the Workout
        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workout)))
            .andExpect(status().isCreated());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate + 1);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkout.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkout.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWorkout.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWorkout.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWorkout.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testWorkout.getPreviewTime()).isEqualTo(DEFAULT_PREVIEW_TIME);
        assertThat(testWorkout.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWorkout.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWorkout.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createWorkoutWithExistingId() throws Exception {
        // Create the Workout with an existing ID
        workout.setId(1L);

        int databaseSizeBeforeCreate = workoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workout)))
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutRepository.findAll().size();
        // set the field null
        workout.setUuid(null);

        // Create the Workout, which fails.

        restWorkoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workout)))
            .andExpect(status().isBadRequest());

        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkouts() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workout.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].previewTime").value(hasItem(DEFAULT_PREVIEW_TIME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get the workout
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, workout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workout.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME))
            .andExpect(jsonPath("$.previewTime").value(DEFAULT_PREVIEW_TIME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getWorkoutsByIdFiltering() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        Long id = workout.getId();

        defaultWorkoutShouldBeFound("id.equals=" + id);
        defaultWorkoutShouldNotBeFound("id.notEquals=" + id);

        defaultWorkoutShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkoutShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkoutShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkoutShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkoutsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where uuid equals to DEFAULT_UUID
        defaultWorkoutShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the workoutList where uuid equals to UPDATED_UUID
        defaultWorkoutShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where uuid not equals to DEFAULT_UUID
        defaultWorkoutShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the workoutList where uuid not equals to UPDATED_UUID
        defaultWorkoutShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultWorkoutShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the workoutList where uuid equals to UPDATED_UUID
        defaultWorkoutShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where uuid is not null
        defaultWorkoutShouldBeFound("uuid.specified=true");

        // Get all the workoutList where uuid is null
        defaultWorkoutShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name equals to DEFAULT_NAME
        defaultWorkoutShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the workoutList where name equals to UPDATED_NAME
        defaultWorkoutShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name not equals to DEFAULT_NAME
        defaultWorkoutShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the workoutList where name not equals to UPDATED_NAME
        defaultWorkoutShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWorkoutShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the workoutList where name equals to UPDATED_NAME
        defaultWorkoutShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name is not null
        defaultWorkoutShouldBeFound("name.specified=true");

        // Get all the workoutList where name is null
        defaultWorkoutShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameContainsSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name contains DEFAULT_NAME
        defaultWorkoutShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the workoutList where name contains UPDATED_NAME
        defaultWorkoutShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where name does not contain DEFAULT_NAME
        defaultWorkoutShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the workoutList where name does not contain UPDATED_NAME
        defaultWorkoutShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where type equals to DEFAULT_TYPE
        defaultWorkoutShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the workoutList where type equals to UPDATED_TYPE
        defaultWorkoutShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where type not equals to DEFAULT_TYPE
        defaultWorkoutShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the workoutList where type not equals to UPDATED_TYPE
        defaultWorkoutShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultWorkoutShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the workoutList where type equals to UPDATED_TYPE
        defaultWorkoutShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where type is not null
        defaultWorkoutShouldBeFound("type.specified=true");

        // Get all the workoutList where type is null
        defaultWorkoutShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime equals to DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.equals=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime equals to UPDATED_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.equals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime not equals to DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.notEquals=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime not equals to UPDATED_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.notEquals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime in DEFAULT_EXECUTION_TIME or UPDATED_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.in=" + DEFAULT_EXECUTION_TIME + "," + UPDATED_EXECUTION_TIME);

        // Get all the workoutList where executionTime equals to UPDATED_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.in=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime is not null
        defaultWorkoutShouldBeFound("executionTime.specified=true");

        // Get all the workoutList where executionTime is null
        defaultWorkoutShouldNotBeFound("executionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime is greater than or equal to DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.greaterThanOrEqual=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime is greater than or equal to UPDATED_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.greaterThanOrEqual=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime is less than or equal to DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.lessThanOrEqual=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime is less than or equal to SMALLER_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.lessThanOrEqual=" + SMALLER_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime is less than DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.lessThan=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime is less than UPDATED_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.lessThan=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByExecutionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where executionTime is greater than DEFAULT_EXECUTION_TIME
        defaultWorkoutShouldNotBeFound("executionTime.greaterThan=" + DEFAULT_EXECUTION_TIME);

        // Get all the workoutList where executionTime is greater than SMALLER_EXECUTION_TIME
        defaultWorkoutShouldBeFound("executionTime.greaterThan=" + SMALLER_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime equals to DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.equals=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime equals to UPDATED_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.equals=" + UPDATED_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime not equals to DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.notEquals=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime not equals to UPDATED_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.notEquals=" + UPDATED_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime in DEFAULT_PREVIEW_TIME or UPDATED_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.in=" + DEFAULT_PREVIEW_TIME + "," + UPDATED_PREVIEW_TIME);

        // Get all the workoutList where previewTime equals to UPDATED_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.in=" + UPDATED_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime is not null
        defaultWorkoutShouldBeFound("previewTime.specified=true");

        // Get all the workoutList where previewTime is null
        defaultWorkoutShouldNotBeFound("previewTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime is greater than or equal to DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.greaterThanOrEqual=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime is greater than or equal to UPDATED_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.greaterThanOrEqual=" + UPDATED_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime is less than or equal to DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.lessThanOrEqual=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime is less than or equal to SMALLER_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.lessThanOrEqual=" + SMALLER_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime is less than DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.lessThan=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime is less than UPDATED_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.lessThan=" + UPDATED_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByPreviewTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where previewTime is greater than DEFAULT_PREVIEW_TIME
        defaultWorkoutShouldNotBeFound("previewTime.greaterThan=" + DEFAULT_PREVIEW_TIME);

        // Get all the workoutList where previewTime is greater than SMALLER_PREVIEW_TIME
        defaultWorkoutShouldBeFound("previewTime.greaterThan=" + SMALLER_PREVIEW_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where status equals to DEFAULT_STATUS
        defaultWorkoutShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the workoutList where status equals to UPDATED_STATUS
        defaultWorkoutShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where status not equals to DEFAULT_STATUS
        defaultWorkoutShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the workoutList where status not equals to UPDATED_STATUS
        defaultWorkoutShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWorkoutShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the workoutList where status equals to UPDATED_STATUS
        defaultWorkoutShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkoutsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where status is not null
        defaultWorkoutShouldBeFound("status.specified=true");

        // Get all the workoutList where status is null
        defaultWorkoutShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date equals to DEFAULT_DATE
        defaultWorkoutShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the workoutList where date equals to UPDATED_DATE
        defaultWorkoutShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date not equals to DEFAULT_DATE
        defaultWorkoutShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the workoutList where date not equals to UPDATED_DATE
        defaultWorkoutShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date in DEFAULT_DATE or UPDATED_DATE
        defaultWorkoutShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the workoutList where date equals to UPDATED_DATE
        defaultWorkoutShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date is not null
        defaultWorkoutShouldBeFound("date.specified=true");

        // Get all the workoutList where date is null
        defaultWorkoutShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date is greater than or equal to DEFAULT_DATE
        defaultWorkoutShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the workoutList where date is greater than or equal to UPDATED_DATE
        defaultWorkoutShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date is less than or equal to DEFAULT_DATE
        defaultWorkoutShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the workoutList where date is less than or equal to SMALLER_DATE
        defaultWorkoutShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date is less than DEFAULT_DATE
        defaultWorkoutShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the workoutList where date is less than UPDATED_DATE
        defaultWorkoutShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where date is greater than DEFAULT_DATE
        defaultWorkoutShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the workoutList where date is greater than SMALLER_DATE
        defaultWorkoutShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note equals to DEFAULT_NOTE
        defaultWorkoutShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the workoutList where note equals to UPDATED_NOTE
        defaultWorkoutShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note not equals to DEFAULT_NOTE
        defaultWorkoutShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the workoutList where note not equals to UPDATED_NOTE
        defaultWorkoutShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultWorkoutShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the workoutList where note equals to UPDATED_NOTE
        defaultWorkoutShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note is not null
        defaultWorkoutShouldBeFound("note.specified=true");

        // Get all the workoutList where note is null
        defaultWorkoutShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteContainsSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note contains DEFAULT_NOTE
        defaultWorkoutShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the workoutList where note contains UPDATED_NOTE
        defaultWorkoutShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        // Get all the workoutList where note does not contain DEFAULT_NOTE
        defaultWorkoutShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the workoutList where note does not contain UPDATED_NOTE
        defaultWorkoutShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllWorkoutsByWorkoutStepIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);
        WorkoutStep workoutStep = WorkoutStepResourceIT.createEntity(em);
        em.persist(workoutStep);
        em.flush();
        workout.addWorkoutStep(workoutStep);
        workoutRepository.saveAndFlush(workout);
        Long workoutStepId = workoutStep.getId();

        // Get all the workoutList where workoutStep equals to workoutStepId
        defaultWorkoutShouldBeFound("workoutStepId.equals=" + workoutStepId);

        // Get all the workoutList where workoutStep equals to (workoutStepId + 1)
        defaultWorkoutShouldNotBeFound("workoutStepId.equals=" + (workoutStepId + 1));
    }

    @Test
    @Transactional
    void getAllWorkoutsByCalendarIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);
        Calendar calendar = CalendarResourceIT.createEntity(em);
        em.persist(calendar);
        em.flush();
        workout.setCalendar(calendar);
        workoutRepository.saveAndFlush(workout);
        Long calendarId = calendar.getId();

        // Get all the workoutList where calendar equals to calendarId
        defaultWorkoutShouldBeFound("calendarId.equals=" + calendarId);

        // Get all the workoutList where calendar equals to (calendarId + 1)
        defaultWorkoutShouldNotBeFound("calendarId.equals=" + (calendarId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkoutShouldBeFound(String filter) throws Exception {
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workout.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME)))
            .andExpect(jsonPath("$.[*].previewTime").value(hasItem(DEFAULT_PREVIEW_TIME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkoutShouldNotBeFound(String filter) throws Exception {
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkout() throws Exception {
        // Get the workout
        restWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout
        Workout updatedWorkout = workoutRepository.findById(workout.getId()).get();
        // Disconnect from session so that the updates on updatedWorkout are not directly saved in db
        em.detach(updatedWorkout);
        updatedWorkout
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .type(UPDATED_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .previewTime(UPDATED_PREVIEW_TIME)
            .status(UPDATED_STATUS)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE);

        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkout.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkout.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWorkout.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWorkout.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkout.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkout.getPreviewTime()).isEqualTo(UPDATED_PREVIEW_TIME);
        assertThat(testWorkout.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkout.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkout.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout.uuid(UPDATED_UUID).previewTime(UPDATED_PREVIEW_TIME).note(UPDATED_NOTE);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkout.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkout.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWorkout.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWorkout.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWorkout.getExecutionTime()).isEqualTo(DEFAULT_EXECUTION_TIME);
        assertThat(testWorkout.getPreviewTime()).isEqualTo(UPDATED_PREVIEW_TIME);
        assertThat(testWorkout.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWorkout.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWorkout.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutWithPatch() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();

        // Update the workout using partial update
        Workout partialUpdatedWorkout = new Workout();
        partialUpdatedWorkout.setId(workout.getId());

        partialUpdatedWorkout
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .type(UPDATED_TYPE)
            .executionTime(UPDATED_EXECUTION_TIME)
            .previewTime(UPDATED_PREVIEW_TIME)
            .status(UPDATED_STATUS)
            .date(UPDATED_DATE)
            .note(UPDATED_NOTE);

        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkout))
            )
            .andExpect(status().isOk());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
        Workout testWorkout = workoutList.get(workoutList.size() - 1);
        assertThat(testWorkout.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkout.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkout.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWorkout.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWorkout.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkout.getExecutionTime()).isEqualTo(UPDATED_EXECUTION_TIME);
        assertThat(testWorkout.getPreviewTime()).isEqualTo(UPDATED_PREVIEW_TIME);
        assertThat(testWorkout.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkout.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkout.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkout() throws Exception {
        int databaseSizeBeforeUpdate = workoutRepository.findAll().size();
        workout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workout in the database
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkout() throws Exception {
        // Initialize the database
        workoutRepository.saveAndFlush(workout);

        int databaseSizeBeforeDelete = workoutRepository.findAll().size();

        // Delete the workout
        restWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, workout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Workout> workoutList = workoutRepository.findAll();
        assertThat(workoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
