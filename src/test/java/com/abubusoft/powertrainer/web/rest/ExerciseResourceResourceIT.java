package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.domain.ExerciseResource;
import com.abubusoft.powertrainer.domain.enumeration.ExerciseResourceType;
import com.abubusoft.powertrainer.repository.ExerciseResourceRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseResourceCriteria;
import com.abubusoft.powertrainer.service.dto.ExerciseResourceDTO;
import com.abubusoft.powertrainer.service.mapper.ExerciseResourceMapper;
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
 * Integration tests for the {@link ExerciseResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseResourceResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final ExerciseResourceType DEFAULT_TYPE = ExerciseResourceType.VIDEO;
    private static final ExerciseResourceType UPDATED_TYPE = ExerciseResourceType.IMAGE;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exercise-resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseResourceRepository exerciseResourceRepository;

    @Autowired
    private ExerciseResourceMapper exerciseResourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseResourceMockMvc;

    private ExerciseResource exerciseResource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseResource createEntity(EntityManager em) {
        ExerciseResource exerciseResource = new ExerciseResource()
            .uuid(DEFAULT_UUID)
            .order(DEFAULT_ORDER)
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return exerciseResource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseResource createUpdatedEntity(EntityManager em) {
        ExerciseResource exerciseResource = new ExerciseResource()
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return exerciseResource;
    }

    @BeforeEach
    public void initTest() {
        exerciseResource = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseResource() throws Exception {
        int databaseSizeBeforeCreate = exerciseResourceRepository.findAll().size();
        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);
        restExerciseResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseResource testExerciseResource = exerciseResourceList.get(exerciseResourceList.size() - 1);
        assertThat(testExerciseResource.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseResource.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testExerciseResource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExerciseResource.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testExerciseResource.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExerciseResource.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseResource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createExerciseResourceWithExistingId() throws Exception {
        // Create the ExerciseResource with an existing ID
        exerciseResource.setId(1L);
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        int databaseSizeBeforeCreate = exerciseResourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseResourceRepository.findAll().size();
        // set the field null
        exerciseResource.setUuid(null);

        // Create the ExerciseResource, which fails.
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        restExerciseResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseResourceRepository.findAll().size();
        // set the field null
        exerciseResource.setType(null);

        // Create the ExerciseResource, which fails.
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        restExerciseResourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExerciseResources() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getExerciseResource() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get the exerciseResource
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseResource.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getExerciseResourcesByIdFiltering() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        Long id = exerciseResource.getId();

        defaultExerciseResourceShouldBeFound("id.equals=" + id);
        defaultExerciseResourceShouldNotBeFound("id.notEquals=" + id);

        defaultExerciseResourceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExerciseResourceShouldNotBeFound("id.greaterThan=" + id);

        defaultExerciseResourceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExerciseResourceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where uuid equals to DEFAULT_UUID
        defaultExerciseResourceShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the exerciseResourceList where uuid equals to UPDATED_UUID
        defaultExerciseResourceShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where uuid not equals to DEFAULT_UUID
        defaultExerciseResourceShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the exerciseResourceList where uuid not equals to UPDATED_UUID
        defaultExerciseResourceShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultExerciseResourceShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the exerciseResourceList where uuid equals to UPDATED_UUID
        defaultExerciseResourceShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where uuid is not null
        defaultExerciseResourceShouldBeFound("uuid.specified=true");

        // Get all the exerciseResourceList where uuid is null
        defaultExerciseResourceShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order equals to DEFAULT_ORDER
        defaultExerciseResourceShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order equals to UPDATED_ORDER
        defaultExerciseResourceShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order not equals to DEFAULT_ORDER
        defaultExerciseResourceShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order not equals to UPDATED_ORDER
        defaultExerciseResourceShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultExerciseResourceShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the exerciseResourceList where order equals to UPDATED_ORDER
        defaultExerciseResourceShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order is not null
        defaultExerciseResourceShouldBeFound("order.specified=true");

        // Get all the exerciseResourceList where order is null
        defaultExerciseResourceShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order is greater than or equal to DEFAULT_ORDER
        defaultExerciseResourceShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order is greater than or equal to UPDATED_ORDER
        defaultExerciseResourceShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order is less than or equal to DEFAULT_ORDER
        defaultExerciseResourceShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order is less than or equal to SMALLER_ORDER
        defaultExerciseResourceShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order is less than DEFAULT_ORDER
        defaultExerciseResourceShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order is less than UPDATED_ORDER
        defaultExerciseResourceShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where order is greater than DEFAULT_ORDER
        defaultExerciseResourceShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the exerciseResourceList where order is greater than SMALLER_ORDER
        defaultExerciseResourceShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where type equals to DEFAULT_TYPE
        defaultExerciseResourceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the exerciseResourceList where type equals to UPDATED_TYPE
        defaultExerciseResourceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where type not equals to DEFAULT_TYPE
        defaultExerciseResourceShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the exerciseResourceList where type not equals to UPDATED_TYPE
        defaultExerciseResourceShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultExerciseResourceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the exerciseResourceList where type equals to UPDATED_TYPE
        defaultExerciseResourceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where type is not null
        defaultExerciseResourceShouldBeFound("type.specified=true");

        // Get all the exerciseResourceList where type is null
        defaultExerciseResourceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url equals to DEFAULT_URL
        defaultExerciseResourceShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the exerciseResourceList where url equals to UPDATED_URL
        defaultExerciseResourceShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url not equals to DEFAULT_URL
        defaultExerciseResourceShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the exerciseResourceList where url not equals to UPDATED_URL
        defaultExerciseResourceShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url in DEFAULT_URL or UPDATED_URL
        defaultExerciseResourceShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the exerciseResourceList where url equals to UPDATED_URL
        defaultExerciseResourceShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url is not null
        defaultExerciseResourceShouldBeFound("url.specified=true");

        // Get all the exerciseResourceList where url is null
        defaultExerciseResourceShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlContainsSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url contains DEFAULT_URL
        defaultExerciseResourceShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the exerciseResourceList where url contains UPDATED_URL
        defaultExerciseResourceShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        // Get all the exerciseResourceList where url does not contain DEFAULT_URL
        defaultExerciseResourceShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the exerciseResourceList where url does not contain UPDATED_URL
        defaultExerciseResourceShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllExerciseResourcesByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);
        Exercise exercise = ExerciseResourceIT.createEntity(em);
        em.persist(exercise);
        em.flush();
        exerciseResource.setExercise(exercise);
        exerciseResourceRepository.saveAndFlush(exerciseResource);
        Long exerciseId = exercise.getId();

        // Get all the exerciseResourceList where exercise equals to exerciseId
        defaultExerciseResourceShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the exerciseResourceList where exercise equals to (exerciseId + 1)
        defaultExerciseResourceShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseResourceShouldBeFound(String filter) throws Exception {
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseResourceShouldNotBeFound(String filter) throws Exception {
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseResourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExerciseResource() throws Exception {
        // Get the exerciseResource
        restExerciseResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseResource() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();

        // Update the exerciseResource
        ExerciseResource updatedExerciseResource = exerciseResourceRepository.findById(exerciseResource.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseResource are not directly saved in db
        em.detach(updatedExerciseResource);
        updatedExerciseResource
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(updatedExerciseResource);

        restExerciseResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseResourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
        ExerciseResource testExerciseResource = exerciseResourceList.get(exerciseResourceList.size() - 1);
        assertThat(testExerciseResource.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseResource.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testExerciseResource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExerciseResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testExerciseResource.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseResource.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseResourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseResourceWithPatch() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();

        // Update the exerciseResource using partial update
        ExerciseResource partialUpdatedExerciseResource = new ExerciseResource();
        partialUpdatedExerciseResource.setId(exerciseResource.getId());

        partialUpdatedExerciseResource
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restExerciseResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseResource))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
        ExerciseResource testExerciseResource = exerciseResourceList.get(exerciseResourceList.size() - 1);
        assertThat(testExerciseResource.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseResource.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testExerciseResource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExerciseResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testExerciseResource.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseResource.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateExerciseResourceWithPatch() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();

        // Update the exerciseResource using partial update
        ExerciseResource partialUpdatedExerciseResource = new ExerciseResource();
        partialUpdatedExerciseResource.setId(exerciseResource.getId());

        partialUpdatedExerciseResource
            .uuid(UPDATED_UUID)
            .order(UPDATED_ORDER)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restExerciseResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseResource))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
        ExerciseResource testExerciseResource = exerciseResourceList.get(exerciseResourceList.size() - 1);
        assertThat(testExerciseResource.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseResource.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testExerciseResource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExerciseResource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testExerciseResource.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseResource.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseResourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseResource() throws Exception {
        int databaseSizeBeforeUpdate = exerciseResourceRepository.findAll().size();
        exerciseResource.setId(count.incrementAndGet());

        // Create the ExerciseResource
        ExerciseResourceDTO exerciseResourceDTO = exerciseResourceMapper.toDto(exerciseResource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseResourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseResourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseResource in the database
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseResource() throws Exception {
        // Initialize the database
        exerciseResourceRepository.saveAndFlush(exerciseResource);

        int databaseSizeBeforeDelete = exerciseResourceRepository.findAll().size();

        // Delete the exerciseResource
        restExerciseResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseResource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseResource> exerciseResourceList = exerciseResourceRepository.findAll();
        assertThat(exerciseResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
