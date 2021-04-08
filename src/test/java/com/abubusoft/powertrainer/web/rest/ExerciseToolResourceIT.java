package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.domain.ExerciseTool;
import com.abubusoft.powertrainer.repository.ExerciseToolRepository;
import com.abubusoft.powertrainer.service.criteria.ExerciseToolCriteria;
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
 * Integration tests for the {@link ExerciseToolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseToolResourceIT {

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

    private static final String ENTITY_API_URL = "/api/exercise-tools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseToolRepository exerciseToolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseToolMockMvc;

    private ExerciseTool exerciseTool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseTool createEntity(EntityManager em) {
        ExerciseTool exerciseTool = new ExerciseTool()
            .uuid(DEFAULT_UUID)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return exerciseTool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseTool createUpdatedEntity(EntityManager em) {
        ExerciseTool exerciseTool = new ExerciseTool()
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return exerciseTool;
    }

    @BeforeEach
    public void initTest() {
        exerciseTool = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseTool() throws Exception {
        int databaseSizeBeforeCreate = exerciseToolRepository.findAll().size();
        // Create the ExerciseTool
        restExerciseToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseTool)))
            .andExpect(status().isCreated());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseTool testExerciseTool = exerciseToolList.get(exerciseToolList.size() - 1);
        assertThat(testExerciseTool.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseTool.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExerciseTool.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseTool.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExerciseTool.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createExerciseToolWithExistingId() throws Exception {
        // Create the ExerciseTool with an existing ID
        exerciseTool.setId(1L);

        int databaseSizeBeforeCreate = exerciseToolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseTool)))
            .andExpect(status().isBadRequest());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseToolRepository.findAll().size();
        // set the field null
        exerciseTool.setUuid(null);

        // Create the ExerciseTool, which fails.

        restExerciseToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseTool)))
            .andExpect(status().isBadRequest());

        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseToolRepository.findAll().size();
        // set the field null
        exerciseTool.setName(null);

        // Create the ExerciseTool, which fails.

        restExerciseToolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseTool)))
            .andExpect(status().isBadRequest());

        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExerciseTools() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseTool.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getExerciseTool() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get the exerciseTool
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseTool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseTool.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getExerciseToolsByIdFiltering() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        Long id = exerciseTool.getId();

        defaultExerciseToolShouldBeFound("id.equals=" + id);
        defaultExerciseToolShouldNotBeFound("id.notEquals=" + id);

        defaultExerciseToolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExerciseToolShouldNotBeFound("id.greaterThan=" + id);

        defaultExerciseToolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExerciseToolShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where uuid equals to DEFAULT_UUID
        defaultExerciseToolShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the exerciseToolList where uuid equals to UPDATED_UUID
        defaultExerciseToolShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where uuid not equals to DEFAULT_UUID
        defaultExerciseToolShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the exerciseToolList where uuid not equals to UPDATED_UUID
        defaultExerciseToolShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultExerciseToolShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the exerciseToolList where uuid equals to UPDATED_UUID
        defaultExerciseToolShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where uuid is not null
        defaultExerciseToolShouldBeFound("uuid.specified=true");

        // Get all the exerciseToolList where uuid is null
        defaultExerciseToolShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name equals to DEFAULT_NAME
        defaultExerciseToolShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the exerciseToolList where name equals to UPDATED_NAME
        defaultExerciseToolShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name not equals to DEFAULT_NAME
        defaultExerciseToolShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the exerciseToolList where name not equals to UPDATED_NAME
        defaultExerciseToolShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExerciseToolShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the exerciseToolList where name equals to UPDATED_NAME
        defaultExerciseToolShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name is not null
        defaultExerciseToolShouldBeFound("name.specified=true");

        // Get all the exerciseToolList where name is null
        defaultExerciseToolShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameContainsSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name contains DEFAULT_NAME
        defaultExerciseToolShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the exerciseToolList where name contains UPDATED_NAME
        defaultExerciseToolShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        // Get all the exerciseToolList where name does not contain DEFAULT_NAME
        defaultExerciseToolShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the exerciseToolList where name does not contain UPDATED_NAME
        defaultExerciseToolShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExerciseToolsByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);
        Exercise exercise = ExerciseResourceIT.createEntity(em);
        em.persist(exercise);
        em.flush();
        exerciseTool.setExercise(exercise);
        exerciseToolRepository.saveAndFlush(exerciseTool);
        Long exerciseId = exercise.getId();

        // Get all the exerciseToolList where exercise equals to exerciseId
        defaultExerciseToolShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the exerciseToolList where exercise equals to (exerciseId + 1)
        defaultExerciseToolShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseToolShouldBeFound(String filter) throws Exception {
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseTool.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseToolShouldNotBeFound(String filter) throws Exception {
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseToolMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExerciseTool() throws Exception {
        // Get the exerciseTool
        restExerciseToolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseTool() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();

        // Update the exerciseTool
        ExerciseTool updatedExerciseTool = exerciseToolRepository.findById(exerciseTool.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseTool are not directly saved in db
        em.detach(updatedExerciseTool);
        updatedExerciseTool
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restExerciseToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseTool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseTool))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
        ExerciseTool testExerciseTool = exerciseToolList.get(exerciseToolList.size() - 1);
        assertThat(testExerciseTool.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseTool.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseTool.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseTool.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseTool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseTool))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseTool))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseTool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseToolWithPatch() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();

        // Update the exerciseTool using partial update
        ExerciseTool partialUpdatedExerciseTool = new ExerciseTool();
        partialUpdatedExerciseTool.setId(exerciseTool.getId());

        partialUpdatedExerciseTool.name(UPDATED_NAME);

        restExerciseToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseTool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseTool))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
        ExerciseTool testExerciseTool = exerciseToolList.get(exerciseToolList.size() - 1);
        assertThat(testExerciseTool.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testExerciseTool.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testExerciseTool.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseTool.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateExerciseToolWithPatch() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();

        // Update the exerciseTool using partial update
        ExerciseTool partialUpdatedExerciseTool = new ExerciseTool();
        partialUpdatedExerciseTool.setId(exerciseTool.getId());

        partialUpdatedExerciseTool
            .uuid(UPDATED_UUID)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restExerciseToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseTool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseTool))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
        ExerciseTool testExerciseTool = exerciseToolList.get(exerciseToolList.size() - 1);
        assertThat(testExerciseTool.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testExerciseTool.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testExerciseTool.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testExerciseTool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseTool.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseTool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseTool))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseTool))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseTool() throws Exception {
        int databaseSizeBeforeUpdate = exerciseToolRepository.findAll().size();
        exerciseTool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseToolMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseTool))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseTool in the database
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseTool() throws Exception {
        // Initialize the database
        exerciseToolRepository.saveAndFlush(exerciseTool);

        int databaseSizeBeforeDelete = exerciseToolRepository.findAll().size();

        // Delete the exerciseTool
        restExerciseToolMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseTool.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseTool> exerciseToolList = exerciseToolRepository.findAll();
        assertThat(exerciseToolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
