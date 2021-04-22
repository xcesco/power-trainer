package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.domain.MisurationType;
import com.abubusoft.powertrainer.repository.MisurationTypeRepository;
import com.abubusoft.powertrainer.service.criteria.MisurationTypeCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationTypeDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationTypeMapper;
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
 * Integration tests for the {@link MisurationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MisurationTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/misuration-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MisurationTypeRepository misurationTypeRepository;

    @Autowired
    private MisurationTypeMapper misurationTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMisurationTypeMockMvc;

    private MisurationType misurationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisurationType createEntity(EntityManager em) {
        MisurationType misurationType = new MisurationType()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return misurationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisurationType createUpdatedEntity(EntityManager em) {
        MisurationType misurationType = new MisurationType()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return misurationType;
    }

    @BeforeEach
    public void initTest() {
        misurationType = createEntity(em);
    }

    @Test
    @Transactional
    void createMisurationType() throws Exception {
        int databaseSizeBeforeCreate = misurationTypeRepository.findAll().size();
        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);
        restMisurationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MisurationType testMisurationType = misurationTypeList.get(misurationTypeList.size() - 1);
        assertThat(testMisurationType.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMisurationType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMisurationType.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMisurationType.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMisurationType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createMisurationTypeWithExistingId() throws Exception {
        // Create the MisurationType with an existing ID
        misurationType.setId(1L);
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        int databaseSizeBeforeCreate = misurationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMisurationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = misurationTypeRepository.findAll().size();
        // set the field null
        misurationType.setUuid(null);

        // Create the MisurationType, which fails.
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        restMisurationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = misurationTypeRepository.findAll().size();
        // set the field null
        misurationType.setName(null);

        // Create the MisurationType, which fails.
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        restMisurationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMisurationTypes() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misurationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getMisurationType() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get the misurationType
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, misurationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(misurationType.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getMisurationTypesByIdFiltering() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        Long id = misurationType.getId();

        defaultMisurationTypeShouldBeFound("id.equals=" + id);
        defaultMisurationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultMisurationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMisurationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultMisurationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMisurationTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where uuid equals to DEFAULT_UUID
        defaultMisurationTypeShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the misurationTypeList where uuid equals to UPDATED_UUID
        defaultMisurationTypeShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where uuid not equals to DEFAULT_UUID
        defaultMisurationTypeShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the misurationTypeList where uuid not equals to UPDATED_UUID
        defaultMisurationTypeShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultMisurationTypeShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the misurationTypeList where uuid equals to UPDATED_UUID
        defaultMisurationTypeShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where uuid is not null
        defaultMisurationTypeShouldBeFound("uuid.specified=true");

        // Get all the misurationTypeList where uuid is null
        defaultMisurationTypeShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name equals to DEFAULT_NAME
        defaultMisurationTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the misurationTypeList where name equals to UPDATED_NAME
        defaultMisurationTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name not equals to DEFAULT_NAME
        defaultMisurationTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the misurationTypeList where name not equals to UPDATED_NAME
        defaultMisurationTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMisurationTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the misurationTypeList where name equals to UPDATED_NAME
        defaultMisurationTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name is not null
        defaultMisurationTypeShouldBeFound("name.specified=true");

        // Get all the misurationTypeList where name is null
        defaultMisurationTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name contains DEFAULT_NAME
        defaultMisurationTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the misurationTypeList where name contains UPDATED_NAME
        defaultMisurationTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        // Get all the misurationTypeList where name does not contain DEFAULT_NAME
        defaultMisurationTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the misurationTypeList where name does not contain UPDATED_NAME
        defaultMisurationTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMisurationTypesByMisurationIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);
        Misuration misuration = MisurationResourceIT.createEntity(em);
        em.persist(misuration);
        em.flush();
        misurationType.addMisuration(misuration);
        misurationTypeRepository.saveAndFlush(misurationType);
        Long misurationId = misuration.getId();

        // Get all the misurationTypeList where misuration equals to misurationId
        defaultMisurationTypeShouldBeFound("misurationId.equals=" + misurationId);

        // Get all the misurationTypeList where misuration equals to (misurationId + 1)
        defaultMisurationTypeShouldNotBeFound("misurationId.equals=" + (misurationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMisurationTypeShouldBeFound(String filter) throws Exception {
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misurationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMisurationTypeShouldNotBeFound(String filter) throws Exception {
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMisurationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMisurationType() throws Exception {
        // Get the misurationType
        restMisurationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMisurationType() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();

        // Update the misurationType
        MisurationType updatedMisurationType = misurationTypeRepository.findById(misurationType.getId()).get();
        // Disconnect from session so that the updates on updatedMisurationType are not directly saved in db
        em.detach(updatedMisurationType);
        updatedMisurationType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(updatedMisurationType);

        restMisurationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misurationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
        MisurationType testMisurationType = misurationTypeList.get(misurationTypeList.size() - 1);
        assertThat(testMisurationType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMisurationType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMisurationType.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMisurationType.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMisurationType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misurationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMisurationTypeWithPatch() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();

        // Update the misurationType using partial update
        MisurationType partialUpdatedMisurationType = new MisurationType();
        partialUpdatedMisurationType.setId(misurationType.getId());

        partialUpdatedMisurationType.uuid(UPDATED_UUID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMisurationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisurationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisurationType))
            )
            .andExpect(status().isOk());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
        MisurationType testMisurationType = misurationTypeList.get(misurationTypeList.size() - 1);
        assertThat(testMisurationType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMisurationType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMisurationType.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMisurationType.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMisurationType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMisurationTypeWithPatch() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();

        // Update the misurationType using partial update
        MisurationType partialUpdatedMisurationType = new MisurationType();
        partialUpdatedMisurationType.setId(misurationType.getId());

        partialUpdatedMisurationType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restMisurationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisurationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisurationType))
            )
            .andExpect(status().isOk());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
        MisurationType testMisurationType = misurationTypeList.get(misurationTypeList.size() - 1);
        assertThat(testMisurationType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMisurationType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMisurationType.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMisurationType.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMisurationType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, misurationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMisurationType() throws Exception {
        int databaseSizeBeforeUpdate = misurationTypeRepository.findAll().size();
        misurationType.setId(count.incrementAndGet());

        // Create the MisurationType
        MisurationTypeDTO misurationTypeDTO = misurationTypeMapper.toDto(misurationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misurationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisurationType in the database
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMisurationType() throws Exception {
        // Initialize the database
        misurationTypeRepository.saveAndFlush(misurationType);

        int databaseSizeBeforeDelete = misurationTypeRepository.findAll().size();

        // Delete the misurationType
        restMisurationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, misurationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MisurationType> misurationTypeList = misurationTypeRepository.findAll();
        assertThat(misurationTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
