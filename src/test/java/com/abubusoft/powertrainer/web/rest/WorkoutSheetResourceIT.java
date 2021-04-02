package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.WorkoutSheet;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import com.abubusoft.powertrainer.repository.WorkoutSheetRepository;
import com.abubusoft.powertrainer.service.criteria.WorkoutSheetCriteria;
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
 * Integration tests for the {@link WorkoutSheetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkoutSheetResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREPARE_TIME = 1;
    private static final Integer UPDATED_PREPARE_TIME = 2;
    private static final Integer SMALLER_PREPARE_TIME = 1 - 1;

    private static final Integer DEFAULT_COOL_DOWN_TIME = 1;
    private static final Integer UPDATED_COOL_DOWN_TIME = 2;
    private static final Integer SMALLER_COOL_DOWN_TIME = 1 - 1;

    private static final Integer DEFAULT_CYCLES = 1;
    private static final Integer UPDATED_CYCLES = 2;
    private static final Integer SMALLER_CYCLES = 1 - 1;

    private static final Integer DEFAULT_CYCLE_REST_TIME = 1;
    private static final Integer UPDATED_CYCLE_REST_TIME = 2;
    private static final Integer SMALLER_CYCLE_REST_TIME = 1 - 1;

    private static final Integer DEFAULT_SET = 1;
    private static final Integer UPDATED_SET = 2;
    private static final Integer SMALLER_SET = 1 - 1;

    private static final Integer DEFAULT_SET_REST_TIME = 1;
    private static final Integer UPDATED_SET_REST_TIME = 2;
    private static final Integer SMALLER_SET_REST_TIME = 1 - 1;

    private static final WorkoutType DEFAULT_TYPE = WorkoutType.SEQUENCE;
    private static final WorkoutType UPDATED_TYPE = WorkoutType.SUPERSET;

    private static final String ENTITY_API_URL = "/api/workout-sheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkoutSheetRepository workoutSheetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkoutSheetMockMvc;

    private WorkoutSheet workoutSheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutSheet createEntity(EntityManager em) {
        WorkoutSheet workoutSheet = new WorkoutSheet()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .prepareTime(DEFAULT_PREPARE_TIME)
            .coolDownTime(DEFAULT_COOL_DOWN_TIME)
            .cycles(DEFAULT_CYCLES)
            .cycleRestTime(DEFAULT_CYCLE_REST_TIME)
            .set(DEFAULT_SET)
            .setRestTime(DEFAULT_SET_REST_TIME)
            .type(DEFAULT_TYPE);
        return workoutSheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkoutSheet createUpdatedEntity(EntityManager em) {
        WorkoutSheet workoutSheet = new WorkoutSheet()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .prepareTime(UPDATED_PREPARE_TIME)
            .coolDownTime(UPDATED_COOL_DOWN_TIME)
            .cycles(UPDATED_CYCLES)
            .cycleRestTime(UPDATED_CYCLE_REST_TIME)
            .set(UPDATED_SET)
            .setRestTime(UPDATED_SET_REST_TIME)
            .type(UPDATED_TYPE);
        return workoutSheet;
    }

    @BeforeEach
    public void initTest() {
        workoutSheet = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkoutSheet() throws Exception {
        int databaseSizeBeforeCreate = workoutSheetRepository.findAll().size();
        // Create the WorkoutSheet
        restWorkoutSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheet)))
            .andExpect(status().isCreated());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeCreate + 1);
        WorkoutSheet testWorkoutSheet = workoutSheetList.get(workoutSheetList.size() - 1);
        assertThat(testWorkoutSheet.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testWorkoutSheet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkoutSheet.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWorkoutSheet.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWorkoutSheet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkoutSheet.getPrepareTime()).isEqualTo(DEFAULT_PREPARE_TIME);
        assertThat(testWorkoutSheet.getCoolDownTime()).isEqualTo(DEFAULT_COOL_DOWN_TIME);
        assertThat(testWorkoutSheet.getCycles()).isEqualTo(DEFAULT_CYCLES);
        assertThat(testWorkoutSheet.getCycleRestTime()).isEqualTo(DEFAULT_CYCLE_REST_TIME);
        assertThat(testWorkoutSheet.getSet()).isEqualTo(DEFAULT_SET);
        assertThat(testWorkoutSheet.getSetRestTime()).isEqualTo(DEFAULT_SET_REST_TIME);
        assertThat(testWorkoutSheet.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWorkoutSheetWithExistingId() throws Exception {
        // Create the WorkoutSheet with an existing ID
        workoutSheet.setId(1L);

        int databaseSizeBeforeCreate = workoutSheetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkoutSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheet)))
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetRepository.findAll().size();
        // set the field null
        workoutSheet.setUuid(null);

        // Create the WorkoutSheet, which fails.

        restWorkoutSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheet)))
            .andExpect(status().isBadRequest());

        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workoutSheetRepository.findAll().size();
        // set the field null
        workoutSheet.setName(null);

        // Create the WorkoutSheet, which fails.

        restWorkoutSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheet)))
            .andExpect(status().isBadRequest());

        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkoutSheets() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].prepareTime").value(hasItem(DEFAULT_PREPARE_TIME)))
            .andExpect(jsonPath("$.[*].coolDownTime").value(hasItem(DEFAULT_COOL_DOWN_TIME)))
            .andExpect(jsonPath("$.[*].cycles").value(hasItem(DEFAULT_CYCLES)))
            .andExpect(jsonPath("$.[*].cycleRestTime").value(hasItem(DEFAULT_CYCLE_REST_TIME)))
            .andExpect(jsonPath("$.[*].set").value(hasItem(DEFAULT_SET)))
            .andExpect(jsonPath("$.[*].setRestTime").value(hasItem(DEFAULT_SET_REST_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getWorkoutSheet() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get the workoutSheet
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL_ID, workoutSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workoutSheet.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.prepareTime").value(DEFAULT_PREPARE_TIME))
            .andExpect(jsonPath("$.coolDownTime").value(DEFAULT_COOL_DOWN_TIME))
            .andExpect(jsonPath("$.cycles").value(DEFAULT_CYCLES))
            .andExpect(jsonPath("$.cycleRestTime").value(DEFAULT_CYCLE_REST_TIME))
            .andExpect(jsonPath("$.set").value(DEFAULT_SET))
            .andExpect(jsonPath("$.setRestTime").value(DEFAULT_SET_REST_TIME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getWorkoutSheetsByIdFiltering() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        Long id = workoutSheet.getId();

        defaultWorkoutSheetShouldBeFound("id.equals=" + id);
        defaultWorkoutSheetShouldNotBeFound("id.notEquals=" + id);

        defaultWorkoutSheetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkoutSheetShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkoutSheetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkoutSheetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where uuid equals to DEFAULT_UUID
        defaultWorkoutSheetShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the workoutSheetList where uuid equals to UPDATED_UUID
        defaultWorkoutSheetShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where uuid not equals to DEFAULT_UUID
        defaultWorkoutSheetShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the workoutSheetList where uuid not equals to UPDATED_UUID
        defaultWorkoutSheetShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultWorkoutSheetShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the workoutSheetList where uuid equals to UPDATED_UUID
        defaultWorkoutSheetShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where uuid is not null
        defaultWorkoutSheetShouldBeFound("uuid.specified=true");

        // Get all the workoutSheetList where uuid is null
        defaultWorkoutSheetShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name equals to DEFAULT_NAME
        defaultWorkoutSheetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the workoutSheetList where name equals to UPDATED_NAME
        defaultWorkoutSheetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name not equals to DEFAULT_NAME
        defaultWorkoutSheetShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the workoutSheetList where name not equals to UPDATED_NAME
        defaultWorkoutSheetShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWorkoutSheetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the workoutSheetList where name equals to UPDATED_NAME
        defaultWorkoutSheetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name is not null
        defaultWorkoutSheetShouldBeFound("name.specified=true");

        // Get all the workoutSheetList where name is null
        defaultWorkoutSheetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameContainsSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name contains DEFAULT_NAME
        defaultWorkoutSheetShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the workoutSheetList where name contains UPDATED_NAME
        defaultWorkoutSheetShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where name does not contain DEFAULT_NAME
        defaultWorkoutSheetShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the workoutSheetList where name does not contain UPDATED_NAME
        defaultWorkoutSheetShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime equals to DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.equals=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime equals to UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.equals=" + UPDATED_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime not equals to DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.notEquals=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime not equals to UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.notEquals=" + UPDATED_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime in DEFAULT_PREPARE_TIME or UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.in=" + DEFAULT_PREPARE_TIME + "," + UPDATED_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime equals to UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.in=" + UPDATED_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime is not null
        defaultWorkoutSheetShouldBeFound("prepareTime.specified=true");

        // Get all the workoutSheetList where prepareTime is null
        defaultWorkoutSheetShouldNotBeFound("prepareTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime is greater than or equal to DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.greaterThanOrEqual=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime is greater than or equal to UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.greaterThanOrEqual=" + UPDATED_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime is less than or equal to DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.lessThanOrEqual=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime is less than or equal to SMALLER_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.lessThanOrEqual=" + SMALLER_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime is less than DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.lessThan=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime is less than UPDATED_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.lessThan=" + UPDATED_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByPrepareTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where prepareTime is greater than DEFAULT_PREPARE_TIME
        defaultWorkoutSheetShouldNotBeFound("prepareTime.greaterThan=" + DEFAULT_PREPARE_TIME);

        // Get all the workoutSheetList where prepareTime is greater than SMALLER_PREPARE_TIME
        defaultWorkoutSheetShouldBeFound("prepareTime.greaterThan=" + SMALLER_PREPARE_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime equals to DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.equals=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime equals to UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.equals=" + UPDATED_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime not equals to DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.notEquals=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime not equals to UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.notEquals=" + UPDATED_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime in DEFAULT_COOL_DOWN_TIME or UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.in=" + DEFAULT_COOL_DOWN_TIME + "," + UPDATED_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime equals to UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.in=" + UPDATED_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime is not null
        defaultWorkoutSheetShouldBeFound("coolDownTime.specified=true");

        // Get all the workoutSheetList where coolDownTime is null
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime is greater than or equal to DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.greaterThanOrEqual=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime is greater than or equal to UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.greaterThanOrEqual=" + UPDATED_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime is less than or equal to DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.lessThanOrEqual=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime is less than or equal to SMALLER_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.lessThanOrEqual=" + SMALLER_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime is less than DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.lessThan=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime is less than UPDATED_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.lessThan=" + UPDATED_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCoolDownTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where coolDownTime is greater than DEFAULT_COOL_DOWN_TIME
        defaultWorkoutSheetShouldNotBeFound("coolDownTime.greaterThan=" + DEFAULT_COOL_DOWN_TIME);

        // Get all the workoutSheetList where coolDownTime is greater than SMALLER_COOL_DOWN_TIME
        defaultWorkoutSheetShouldBeFound("coolDownTime.greaterThan=" + SMALLER_COOL_DOWN_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles equals to DEFAULT_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.equals=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles equals to UPDATED_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.equals=" + UPDATED_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles not equals to DEFAULT_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.notEquals=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles not equals to UPDATED_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.notEquals=" + UPDATED_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles in DEFAULT_CYCLES or UPDATED_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.in=" + DEFAULT_CYCLES + "," + UPDATED_CYCLES);

        // Get all the workoutSheetList where cycles equals to UPDATED_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.in=" + UPDATED_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles is not null
        defaultWorkoutSheetShouldBeFound("cycles.specified=true");

        // Get all the workoutSheetList where cycles is null
        defaultWorkoutSheetShouldNotBeFound("cycles.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles is greater than or equal to DEFAULT_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.greaterThanOrEqual=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles is greater than or equal to UPDATED_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.greaterThanOrEqual=" + UPDATED_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles is less than or equal to DEFAULT_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.lessThanOrEqual=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles is less than or equal to SMALLER_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.lessThanOrEqual=" + SMALLER_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles is less than DEFAULT_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.lessThan=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles is less than UPDATED_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.lessThan=" + UPDATED_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCyclesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycles is greater than DEFAULT_CYCLES
        defaultWorkoutSheetShouldNotBeFound("cycles.greaterThan=" + DEFAULT_CYCLES);

        // Get all the workoutSheetList where cycles is greater than SMALLER_CYCLES
        defaultWorkoutSheetShouldBeFound("cycles.greaterThan=" + SMALLER_CYCLES);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime equals to DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.equals=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime equals to UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.equals=" + UPDATED_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime not equals to DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.notEquals=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime not equals to UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.notEquals=" + UPDATED_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime in DEFAULT_CYCLE_REST_TIME or UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.in=" + DEFAULT_CYCLE_REST_TIME + "," + UPDATED_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime equals to UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.in=" + UPDATED_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime is not null
        defaultWorkoutSheetShouldBeFound("cycleRestTime.specified=true");

        // Get all the workoutSheetList where cycleRestTime is null
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime is greater than or equal to DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.greaterThanOrEqual=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime is greater than or equal to UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.greaterThanOrEqual=" + UPDATED_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime is less than or equal to DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.lessThanOrEqual=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime is less than or equal to SMALLER_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.lessThanOrEqual=" + SMALLER_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime is less than DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.lessThan=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime is less than UPDATED_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.lessThan=" + UPDATED_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByCycleRestTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where cycleRestTime is greater than DEFAULT_CYCLE_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("cycleRestTime.greaterThan=" + DEFAULT_CYCLE_REST_TIME);

        // Get all the workoutSheetList where cycleRestTime is greater than SMALLER_CYCLE_REST_TIME
        defaultWorkoutSheetShouldBeFound("cycleRestTime.greaterThan=" + SMALLER_CYCLE_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set equals to DEFAULT_SET
        defaultWorkoutSheetShouldBeFound("set.equals=" + DEFAULT_SET);

        // Get all the workoutSheetList where set equals to UPDATED_SET
        defaultWorkoutSheetShouldNotBeFound("set.equals=" + UPDATED_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set not equals to DEFAULT_SET
        defaultWorkoutSheetShouldNotBeFound("set.notEquals=" + DEFAULT_SET);

        // Get all the workoutSheetList where set not equals to UPDATED_SET
        defaultWorkoutSheetShouldBeFound("set.notEquals=" + UPDATED_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set in DEFAULT_SET or UPDATED_SET
        defaultWorkoutSheetShouldBeFound("set.in=" + DEFAULT_SET + "," + UPDATED_SET);

        // Get all the workoutSheetList where set equals to UPDATED_SET
        defaultWorkoutSheetShouldNotBeFound("set.in=" + UPDATED_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set is not null
        defaultWorkoutSheetShouldBeFound("set.specified=true");

        // Get all the workoutSheetList where set is null
        defaultWorkoutSheetShouldNotBeFound("set.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set is greater than or equal to DEFAULT_SET
        defaultWorkoutSheetShouldBeFound("set.greaterThanOrEqual=" + DEFAULT_SET);

        // Get all the workoutSheetList where set is greater than or equal to UPDATED_SET
        defaultWorkoutSheetShouldNotBeFound("set.greaterThanOrEqual=" + UPDATED_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set is less than or equal to DEFAULT_SET
        defaultWorkoutSheetShouldBeFound("set.lessThanOrEqual=" + DEFAULT_SET);

        // Get all the workoutSheetList where set is less than or equal to SMALLER_SET
        defaultWorkoutSheetShouldNotBeFound("set.lessThanOrEqual=" + SMALLER_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set is less than DEFAULT_SET
        defaultWorkoutSheetShouldNotBeFound("set.lessThan=" + DEFAULT_SET);

        // Get all the workoutSheetList where set is less than UPDATED_SET
        defaultWorkoutSheetShouldBeFound("set.lessThan=" + UPDATED_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where set is greater than DEFAULT_SET
        defaultWorkoutSheetShouldNotBeFound("set.greaterThan=" + DEFAULT_SET);

        // Get all the workoutSheetList where set is greater than SMALLER_SET
        defaultWorkoutSheetShouldBeFound("set.greaterThan=" + SMALLER_SET);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime equals to DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.equals=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime equals to UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.equals=" + UPDATED_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime not equals to DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.notEquals=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime not equals to UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.notEquals=" + UPDATED_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime in DEFAULT_SET_REST_TIME or UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.in=" + DEFAULT_SET_REST_TIME + "," + UPDATED_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime equals to UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.in=" + UPDATED_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime is not null
        defaultWorkoutSheetShouldBeFound("setRestTime.specified=true");

        // Get all the workoutSheetList where setRestTime is null
        defaultWorkoutSheetShouldNotBeFound("setRestTime.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime is greater than or equal to DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.greaterThanOrEqual=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime is greater than or equal to UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.greaterThanOrEqual=" + UPDATED_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime is less than or equal to DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.lessThanOrEqual=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime is less than or equal to SMALLER_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.lessThanOrEqual=" + SMALLER_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime is less than DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.lessThan=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime is less than UPDATED_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.lessThan=" + UPDATED_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsBySetRestTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where setRestTime is greater than DEFAULT_SET_REST_TIME
        defaultWorkoutSheetShouldNotBeFound("setRestTime.greaterThan=" + DEFAULT_SET_REST_TIME);

        // Get all the workoutSheetList where setRestTime is greater than SMALLER_SET_REST_TIME
        defaultWorkoutSheetShouldBeFound("setRestTime.greaterThan=" + SMALLER_SET_REST_TIME);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where type equals to DEFAULT_TYPE
        defaultWorkoutSheetShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the workoutSheetList where type equals to UPDATED_TYPE
        defaultWorkoutSheetShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where type not equals to DEFAULT_TYPE
        defaultWorkoutSheetShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the workoutSheetList where type not equals to UPDATED_TYPE
        defaultWorkoutSheetShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultWorkoutSheetShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the workoutSheetList where type equals to UPDATED_TYPE
        defaultWorkoutSheetShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkoutSheetsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        // Get all the workoutSheetList where type is not null
        defaultWorkoutSheetShouldBeFound("type.specified=true");

        // Get all the workoutSheetList where type is null
        defaultWorkoutSheetShouldNotBeFound("type.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkoutSheetShouldBeFound(String filter) throws Exception {
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workoutSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].prepareTime").value(hasItem(DEFAULT_PREPARE_TIME)))
            .andExpect(jsonPath("$.[*].coolDownTime").value(hasItem(DEFAULT_COOL_DOWN_TIME)))
            .andExpect(jsonPath("$.[*].cycles").value(hasItem(DEFAULT_CYCLES)))
            .andExpect(jsonPath("$.[*].cycleRestTime").value(hasItem(DEFAULT_CYCLE_REST_TIME)))
            .andExpect(jsonPath("$.[*].set").value(hasItem(DEFAULT_SET)))
            .andExpect(jsonPath("$.[*].setRestTime").value(hasItem(DEFAULT_SET_REST_TIME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkoutSheetShouldNotBeFound(String filter) throws Exception {
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkoutSheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkoutSheet() throws Exception {
        // Get the workoutSheet
        restWorkoutSheetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkoutSheet() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();

        // Update the workoutSheet
        WorkoutSheet updatedWorkoutSheet = workoutSheetRepository.findById(workoutSheet.getId()).get();
        // Disconnect from session so that the updates on updatedWorkoutSheet are not directly saved in db
        em.detach(updatedWorkoutSheet);
        updatedWorkoutSheet
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .prepareTime(UPDATED_PREPARE_TIME)
            .coolDownTime(UPDATED_COOL_DOWN_TIME)
            .cycles(UPDATED_CYCLES)
            .cycleRestTime(UPDATED_CYCLE_REST_TIME)
            .set(UPDATED_SET)
            .setRestTime(UPDATED_SET_REST_TIME)
            .type(UPDATED_TYPE);

        restWorkoutSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkoutSheet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkoutSheet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheet testWorkoutSheet = workoutSheetList.get(workoutSheetList.size() - 1);
        assertThat(testWorkoutSheet.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkoutSheet.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWorkoutSheet.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWorkoutSheet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutSheet.getPrepareTime()).isEqualTo(UPDATED_PREPARE_TIME);
        assertThat(testWorkoutSheet.getCoolDownTime()).isEqualTo(UPDATED_COOL_DOWN_TIME);
        assertThat(testWorkoutSheet.getCycles()).isEqualTo(UPDATED_CYCLES);
        assertThat(testWorkoutSheet.getCycleRestTime()).isEqualTo(UPDATED_CYCLE_REST_TIME);
        assertThat(testWorkoutSheet.getSet()).isEqualTo(UPDATED_SET);
        assertThat(testWorkoutSheet.getSetRestTime()).isEqualTo(UPDATED_SET_REST_TIME);
        assertThat(testWorkoutSheet.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workoutSheet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workoutSheet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkoutSheetWithPatch() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();

        // Update the workoutSheet using partial update
        WorkoutSheet partialUpdatedWorkoutSheet = new WorkoutSheet();
        partialUpdatedWorkoutSheet.setId(workoutSheet.getId());

        partialUpdatedWorkoutSheet
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .prepareTime(UPDATED_PREPARE_TIME)
            .set(UPDATED_SET)
            .setRestTime(UPDATED_SET_REST_TIME)
            .type(UPDATED_TYPE);

        restWorkoutSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutSheet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheet testWorkoutSheet = workoutSheetList.get(workoutSheetList.size() - 1);
        assertThat(testWorkoutSheet.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkoutSheet.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWorkoutSheet.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWorkoutSheet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutSheet.getPrepareTime()).isEqualTo(UPDATED_PREPARE_TIME);
        assertThat(testWorkoutSheet.getCoolDownTime()).isEqualTo(DEFAULT_COOL_DOWN_TIME);
        assertThat(testWorkoutSheet.getCycles()).isEqualTo(DEFAULT_CYCLES);
        assertThat(testWorkoutSheet.getCycleRestTime()).isEqualTo(DEFAULT_CYCLE_REST_TIME);
        assertThat(testWorkoutSheet.getSet()).isEqualTo(UPDATED_SET);
        assertThat(testWorkoutSheet.getSetRestTime()).isEqualTo(UPDATED_SET_REST_TIME);
        assertThat(testWorkoutSheet.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkoutSheetWithPatch() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();

        // Update the workoutSheet using partial update
        WorkoutSheet partialUpdatedWorkoutSheet = new WorkoutSheet();
        partialUpdatedWorkoutSheet.setId(workoutSheet.getId());

        partialUpdatedWorkoutSheet
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .prepareTime(UPDATED_PREPARE_TIME)
            .coolDownTime(UPDATED_COOL_DOWN_TIME)
            .cycles(UPDATED_CYCLES)
            .cycleRestTime(UPDATED_CYCLE_REST_TIME)
            .set(UPDATED_SET)
            .setRestTime(UPDATED_SET_REST_TIME)
            .type(UPDATED_TYPE);

        restWorkoutSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkoutSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkoutSheet))
            )
            .andExpect(status().isOk());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
        WorkoutSheet testWorkoutSheet = workoutSheetList.get(workoutSheetList.size() - 1);
        assertThat(testWorkoutSheet.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testWorkoutSheet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkoutSheet.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWorkoutSheet.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWorkoutSheet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkoutSheet.getPrepareTime()).isEqualTo(UPDATED_PREPARE_TIME);
        assertThat(testWorkoutSheet.getCoolDownTime()).isEqualTo(UPDATED_COOL_DOWN_TIME);
        assertThat(testWorkoutSheet.getCycles()).isEqualTo(UPDATED_CYCLES);
        assertThat(testWorkoutSheet.getCycleRestTime()).isEqualTo(UPDATED_CYCLE_REST_TIME);
        assertThat(testWorkoutSheet.getSet()).isEqualTo(UPDATED_SET);
        assertThat(testWorkoutSheet.getSetRestTime()).isEqualTo(UPDATED_SET_REST_TIME);
        assertThat(testWorkoutSheet.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workoutSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workoutSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkoutSheet() throws Exception {
        int databaseSizeBeforeUpdate = workoutSheetRepository.findAll().size();
        workoutSheet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkoutSheetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workoutSheet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkoutSheet in the database
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkoutSheet() throws Exception {
        // Initialize the database
        workoutSheetRepository.saveAndFlush(workoutSheet);

        int databaseSizeBeforeDelete = workoutSheetRepository.findAll().size();

        // Delete the workoutSheet
        restWorkoutSheetMockMvc
            .perform(delete(ENTITY_API_URL_ID, workoutSheet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkoutSheet> workoutSheetList = workoutSheetRepository.findAll();
        assertThat(workoutSheetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
