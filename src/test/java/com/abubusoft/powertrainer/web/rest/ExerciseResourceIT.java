package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.domain.Muscle;
import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.repository.ExerciseRepository;
import com.abubusoft.powertrainer.service.ExerciseService;
import com.abubusoft.powertrainer.service.criteria.ExerciseCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExerciseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExerciseResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.DURATION;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.WEIGHT;

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepositoryMock;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Mock
    private ExerciseService exerciseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseMockMvc;

    private Exercise exercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .uuid(DEFAULT_UUID)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .valueType(DEFAULT_VALUE_TYPE)
            .owner(DEFAULT_OWNER);
        return exercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createUpdatedEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .valueType(UPDATED_VALUE_TYPE)
            .owner(UPDATED_OWNER);
        return exercise;
    }

    @BeforeEach
    public void initTest() {
        exercise = createEntity(em);
    }

    @Test
    @Transactional
    void createExercise() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();
        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);
        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExercise.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExercise.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExercise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExercise.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testExercise.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    void createExerciseWithExistingId() throws Exception {
        // Create the Exercise with an existing ID
        exercise.setId(1L);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setUuid(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setName(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setOwner(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExercises() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExercisesWithEagerRelationshipsIsEnabled() throws Exception {
        when(exerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExerciseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(exerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExercisesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(exerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExerciseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(exerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get the exercise
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exercise.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER));
    }

    @Test
    @Transactional
    void getExercisesByIdFiltering() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        Long id = exercise.getId();

        defaultExerciseShouldBeFound("id.equals=" + id);
        defaultExerciseShouldNotBeFound("id.notEquals=" + id);

        defaultExerciseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExerciseShouldNotBeFound("id.greaterThan=" + id);

        defaultExerciseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExerciseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExercisesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where uuid equals to DEFAULT_UUID
        defaultExerciseShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the exerciseList where uuid equals to UPDATED_UUID
        defaultExerciseShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExercisesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where uuid not equals to DEFAULT_UUID
        defaultExerciseShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the exerciseList where uuid not equals to UPDATED_UUID
        defaultExerciseShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExercisesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultExerciseShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the exerciseList where uuid equals to UPDATED_UUID
        defaultExerciseShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExercisesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where uuid is not null
        defaultExerciseShouldBeFound("uuid.specified=true");

        // Get all the exerciseList where uuid is null
        defaultExerciseShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name equals to DEFAULT_NAME
        defaultExerciseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the exerciseList where name equals to UPDATED_NAME
        defaultExerciseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExercisesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name not equals to DEFAULT_NAME
        defaultExerciseShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the exerciseList where name not equals to UPDATED_NAME
        defaultExerciseShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExercisesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExerciseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the exerciseList where name equals to UPDATED_NAME
        defaultExerciseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExercisesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name is not null
        defaultExerciseShouldBeFound("name.specified=true");

        // Get all the exerciseList where name is null
        defaultExerciseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByNameContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name contains DEFAULT_NAME
        defaultExerciseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the exerciseList where name contains UPDATED_NAME
        defaultExerciseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExercisesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where name does not contain DEFAULT_NAME
        defaultExerciseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the exerciseList where name does not contain UPDATED_NAME
        defaultExerciseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExercisesByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where valueType equals to DEFAULT_VALUE_TYPE
        defaultExerciseShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the exerciseList where valueType equals to UPDATED_VALUE_TYPE
        defaultExerciseShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExercisesByValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where valueType not equals to DEFAULT_VALUE_TYPE
        defaultExerciseShouldNotBeFound("valueType.notEquals=" + DEFAULT_VALUE_TYPE);

        // Get all the exerciseList where valueType not equals to UPDATED_VALUE_TYPE
        defaultExerciseShouldBeFound("valueType.notEquals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExercisesByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultExerciseShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the exerciseList where valueType equals to UPDATED_VALUE_TYPE
        defaultExerciseShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllExercisesByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where valueType is not null
        defaultExerciseShouldBeFound("valueType.specified=true");

        // Get all the exerciseList where valueType is null
        defaultExerciseShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner equals to DEFAULT_OWNER
        defaultExerciseShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the exerciseList where owner equals to UPDATED_OWNER
        defaultExerciseShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner not equals to DEFAULT_OWNER
        defaultExerciseShouldNotBeFound("owner.notEquals=" + DEFAULT_OWNER);

        // Get all the exerciseList where owner not equals to UPDATED_OWNER
        defaultExerciseShouldBeFound("owner.notEquals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultExerciseShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the exerciseList where owner equals to UPDATED_OWNER
        defaultExerciseShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner is not null
        defaultExerciseShouldBeFound("owner.specified=true");

        // Get all the exerciseList where owner is null
        defaultExerciseShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner contains DEFAULT_OWNER
        defaultExerciseShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the exerciseList where owner contains UPDATED_OWNER
        defaultExerciseShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllExercisesByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where owner does not contain DEFAULT_OWNER
        defaultExerciseShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the exerciseList where owner does not contain UPDATED_OWNER
        defaultExerciseShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllExercisesByExerciseResourceIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);
        ExerciseResource exerciseResource = ExerciseResourceResourceIT.createEntity(em);
        em.persist(exerciseResource);
        em.flush();
        exercise.addExerciseResource(exerciseResource);
        exerciseRepository.saveAndFlush(exercise);
        Long exerciseResourceId = exerciseResource.getId();

        // Get all the exerciseList where exerciseResource equals to exerciseResourceId
        defaultExerciseShouldBeFound("exerciseResourceId.equals=" + exerciseResourceId);

        // Get all the exerciseList where exerciseResource equals to (exerciseResourceId + 1)
        defaultExerciseShouldNotBeFound("exerciseResourceId.equals=" + (exerciseResourceId + 1));
    }

    @Test
    @Transactional
    void getAllExercisesByMuscleIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);
        Muscle muscle = MuscleResourceIT.createEntity(em);
        em.persist(muscle);
        em.flush();
        exercise.addMuscle(muscle);
        exerciseRepository.saveAndFlush(exercise);
        Long muscleId = muscle.getId();

        // Get all the exerciseList where muscle equals to muscleId
        defaultExerciseShouldBeFound("muscleId.equals=" + muscleId);

        // Get all the exerciseList where muscle equals to (muscleId + 1)
        defaultExerciseShouldNotBeFound("muscleId.equals=" + (muscleId + 1));
    }

    @Test
    @Transactional
    void getAllExercisesByExerciseToolIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);
        ExerciseTool exerciseTool = ExerciseToolResourceIT.createEntity(em);
        em.persist(exerciseTool);
        em.flush();
        exercise.addExerciseTool(exerciseTool);
        exerciseRepository.saveAndFlush(exercise);
        Long exerciseToolId = exerciseTool.getId();

        // Get all the exerciseList where exerciseTool equals to exerciseToolId
        defaultExerciseShouldBeFound("exerciseToolId.equals=" + exerciseToolId);

        // Get all the exerciseList where exerciseTool equals to (exerciseToolId + 1)
        defaultExerciseShouldNotBeFound("exerciseToolId.equals=" + (exerciseToolId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseShouldBeFound(String filter) throws Exception {
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));

        // Check, that the count call also returns 1
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseShouldNotBeFound(String filter) throws Exception {
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExercise() throws Exception {
        // Get the exercise
        restExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise
        Exercise updatedExercise = exerciseRepository.findById(exercise.getId()).get();
        // Disconnect from session so that the updates on updatedExercise are not directly saved in db
        em.detach(updatedExercise);
        updatedExercise
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .valueType(UPDATED_VALUE_TYPE)
            .owner(UPDATED_OWNER);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(updatedExercise);

        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExercise.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExercise.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testExercise.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void putNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .valueType(UPDATED_VALUE_TYPE)
            .owner(UPDATED_OWNER);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExercise.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExercise.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testExercise.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void fullUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .valueType(UPDATED_VALUE_TYPE)
            .owner(UPDATED_OWNER);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExercise.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExercise.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExercise.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testExercise.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void patchNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeDelete = exerciseRepository.findAll().size();

        // Delete the exercise
        restExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, exercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
