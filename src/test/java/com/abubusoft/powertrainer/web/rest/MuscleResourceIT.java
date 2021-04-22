package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Exercise;
import com.abubusoft.powertrainer.domain.Muscle;
import com.abubusoft.powertrainer.repository.MuscleRepository;
import com.abubusoft.powertrainer.service.criteria.MuscleCriteria;
import com.abubusoft.powertrainer.service.dto.MuscleDTO;
import com.abubusoft.powertrainer.service.mapper.MuscleMapper;
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
 * Integration tests for the {@link MuscleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MuscleResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/muscles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private MuscleMapper muscleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMuscleMockMvc;

    private Muscle muscle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle createEntity(EntityManager em) {
        Muscle muscle = new Muscle()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .note(DEFAULT_NOTE);
        return muscle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle createUpdatedEntity(EntityManager em) {
        Muscle muscle = new Muscle()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);
        return muscle;
    }

    @BeforeEach
    public void initTest() {
        muscle = createEntity(em);
    }

    @Test
    @Transactional
    void createMuscle() throws Exception {
        int databaseSizeBeforeCreate = muscleRepository.findAll().size();
        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);
        restMuscleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscleDTO)))
            .andExpect(status().isCreated());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeCreate + 1);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMuscle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMuscle.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMuscle.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMuscle.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createMuscleWithExistingId() throws Exception {
        // Create the Muscle with an existing ID
        muscle.setId(1L);
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        int databaseSizeBeforeCreate = muscleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuscleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = muscleRepository.findAll().size();
        // set the field null
        muscle.setUuid(null);

        // Create the Muscle, which fails.
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        restMuscleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscleDTO)))
            .andExpect(status().isBadRequest());

        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = muscleRepository.findAll().size();
        // set the field null
        muscle.setName(null);

        // Create the Muscle, which fails.
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        restMuscleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscleDTO)))
            .andExpect(status().isBadRequest());

        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMuscles() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get the muscle
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL_ID, muscle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(muscle.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getMusclesByIdFiltering() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        Long id = muscle.getId();

        defaultMuscleShouldBeFound("id.equals=" + id);
        defaultMuscleShouldNotBeFound("id.notEquals=" + id);

        defaultMuscleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMuscleShouldNotBeFound("id.greaterThan=" + id);

        defaultMuscleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMuscleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMusclesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where uuid equals to DEFAULT_UUID
        defaultMuscleShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the muscleList where uuid equals to UPDATED_UUID
        defaultMuscleShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMusclesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where uuid not equals to DEFAULT_UUID
        defaultMuscleShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the muscleList where uuid not equals to UPDATED_UUID
        defaultMuscleShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMusclesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultMuscleShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the muscleList where uuid equals to UPDATED_UUID
        defaultMuscleShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMusclesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where uuid is not null
        defaultMuscleShouldBeFound("uuid.specified=true");

        // Get all the muscleList where uuid is null
        defaultMuscleShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMusclesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name equals to DEFAULT_NAME
        defaultMuscleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the muscleList where name equals to UPDATED_NAME
        defaultMuscleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMusclesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name not equals to DEFAULT_NAME
        defaultMuscleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the muscleList where name not equals to UPDATED_NAME
        defaultMuscleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMusclesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMuscleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the muscleList where name equals to UPDATED_NAME
        defaultMuscleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMusclesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name is not null
        defaultMuscleShouldBeFound("name.specified=true");

        // Get all the muscleList where name is null
        defaultMuscleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMusclesByNameContainsSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name contains DEFAULT_NAME
        defaultMuscleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the muscleList where name contains UPDATED_NAME
        defaultMuscleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMusclesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where name does not contain DEFAULT_NAME
        defaultMuscleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the muscleList where name does not contain UPDATED_NAME
        defaultMuscleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMusclesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note equals to DEFAULT_NOTE
        defaultMuscleShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the muscleList where note equals to UPDATED_NOTE
        defaultMuscleShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMusclesByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note not equals to DEFAULT_NOTE
        defaultMuscleShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the muscleList where note not equals to UPDATED_NOTE
        defaultMuscleShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMusclesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultMuscleShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the muscleList where note equals to UPDATED_NOTE
        defaultMuscleShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMusclesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note is not null
        defaultMuscleShouldBeFound("note.specified=true");

        // Get all the muscleList where note is null
        defaultMuscleShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllMusclesByNoteContainsSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note contains DEFAULT_NOTE
        defaultMuscleShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the muscleList where note contains UPDATED_NOTE
        defaultMuscleShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMusclesByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        // Get all the muscleList where note does not contain DEFAULT_NOTE
        defaultMuscleShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the muscleList where note does not contain UPDATED_NOTE
        defaultMuscleShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMusclesByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);
        Exercise exercise = ExerciseResourceIT.createEntity(em);
        em.persist(exercise);
        em.flush();
        muscle.addExercise(exercise);
        muscleRepository.saveAndFlush(muscle);
        Long exerciseId = exercise.getId();

        // Get all the muscleList where exercise equals to exerciseId
        defaultMuscleShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the muscleList where exercise equals to (exerciseId + 1)
        defaultMuscleShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMuscleShouldBeFound(String filter) throws Exception {
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMuscleShouldNotBeFound(String filter) throws Exception {
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMuscleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMuscle() throws Exception {
        // Get the muscle
        restMuscleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle
        Muscle updatedMuscle = muscleRepository.findById(muscle.getId()).get();
        // Disconnect from session so that the updates on updatedMuscle are not directly saved in db
        em.detach(updatedMuscle);
        updatedMuscle
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);
        MuscleDTO muscleDTO = muscleMapper.toDto(updatedMuscle);

        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, muscleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMuscle.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMuscle.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, muscleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMuscleWithPatch() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle using partial update
        Muscle partialUpdatedMuscle = new Muscle();
        partialUpdatedMuscle.setId(muscle.getId());

        partialUpdatedMuscle
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMuscle.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMuscle.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateMuscleWithPatch() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();

        // Update the muscle using partial update
        Muscle partialUpdatedMuscle = new Muscle();
        partialUpdatedMuscle.setId(muscle.getId());

        partialUpdatedMuscle
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle))
            )
            .andExpect(status().isOk());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
        Muscle testMuscle = muscleList.get(muscleList.size() - 1);
        assertThat(testMuscle.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMuscle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuscle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMuscle.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMuscle.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, muscleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMuscle() throws Exception {
        int databaseSizeBeforeUpdate = muscleRepository.findAll().size();
        muscle.setId(count.incrementAndGet());

        // Create the Muscle
        MuscleDTO muscleDTO = muscleMapper.toDto(muscle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(muscleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle in the database
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMuscle() throws Exception {
        // Initialize the database
        muscleRepository.saveAndFlush(muscle);

        int databaseSizeBeforeDelete = muscleRepository.findAll().size();

        // Delete the muscle
        restMuscleMockMvc
            .perform(delete(ENTITY_API_URL_ID, muscle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Muscle> muscleList = muscleRepository.findAll();
        assertThat(muscleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
