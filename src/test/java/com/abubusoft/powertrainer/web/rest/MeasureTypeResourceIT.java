package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.MeasureType;
import com.abubusoft.powertrainer.repository.MeasureTypeRepository;
import com.abubusoft.powertrainer.service.criteria.MeasureTypeCriteria;
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
 * Integration tests for the {@link MeasureTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeasureTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/measure-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeasureTypeRepository measureTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasureTypeMockMvc;

    private MeasureType measureType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureType createEntity(EntityManager em) {
        MeasureType measureType = new MeasureType()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .note(DEFAULT_NOTE);
        return measureType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeasureType createUpdatedEntity(EntityManager em) {
        MeasureType measureType = new MeasureType()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);
        return measureType;
    }

    @BeforeEach
    public void initTest() {
        measureType = createEntity(em);
    }

    @Test
    @Transactional
    void createMeasureType() throws Exception {
        int databaseSizeBeforeCreate = measureTypeRepository.findAll().size();
        // Create the MeasureType
        restMeasureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureType)))
            .andExpect(status().isCreated());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MeasureType testMeasureType = measureTypeList.get(measureTypeList.size() - 1);
        assertThat(testMeasureType.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMeasureType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeasureType.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMeasureType.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMeasureType.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createMeasureTypeWithExistingId() throws Exception {
        // Create the MeasureType with an existing ID
        measureType.setId(1L);

        int databaseSizeBeforeCreate = measureTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureType)))
            .andExpect(status().isBadRequest());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureTypeRepository.findAll().size();
        // set the field null
        measureType.setUuid(null);

        // Create the MeasureType, which fails.

        restMeasureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureType)))
            .andExpect(status().isBadRequest());

        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = measureTypeRepository.findAll().size();
        // set the field null
        measureType.setName(null);

        // Create the MeasureType, which fails.

        restMeasureTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureType)))
            .andExpect(status().isBadRequest());

        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasureTypes() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measureType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    void getMeasureType() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get the measureType
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, measureType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measureType.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    void getMeasureTypesByIdFiltering() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        Long id = measureType.getId();

        defaultMeasureTypeShouldBeFound("id.equals=" + id);
        defaultMeasureTypeShouldNotBeFound("id.notEquals=" + id);

        defaultMeasureTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeasureTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultMeasureTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeasureTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where uuid equals to DEFAULT_UUID
        defaultMeasureTypeShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the measureTypeList where uuid equals to UPDATED_UUID
        defaultMeasureTypeShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where uuid not equals to DEFAULT_UUID
        defaultMeasureTypeShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the measureTypeList where uuid not equals to UPDATED_UUID
        defaultMeasureTypeShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultMeasureTypeShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the measureTypeList where uuid equals to UPDATED_UUID
        defaultMeasureTypeShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where uuid is not null
        defaultMeasureTypeShouldBeFound("uuid.specified=true");

        // Get all the measureTypeList where uuid is null
        defaultMeasureTypeShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name equals to DEFAULT_NAME
        defaultMeasureTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the measureTypeList where name equals to UPDATED_NAME
        defaultMeasureTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name not equals to DEFAULT_NAME
        defaultMeasureTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the measureTypeList where name not equals to UPDATED_NAME
        defaultMeasureTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMeasureTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the measureTypeList where name equals to UPDATED_NAME
        defaultMeasureTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name is not null
        defaultMeasureTypeShouldBeFound("name.specified=true");

        // Get all the measureTypeList where name is null
        defaultMeasureTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name contains DEFAULT_NAME
        defaultMeasureTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the measureTypeList where name contains UPDATED_NAME
        defaultMeasureTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasureTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        // Get all the measureTypeList where name does not contain DEFAULT_NAME
        defaultMeasureTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the measureTypeList where name does not contain UPDATED_NAME
        defaultMeasureTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeasureTypeShouldBeFound(String filter) throws Exception {
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measureType.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));

        // Check, that the count call also returns 1
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeasureTypeShouldNotBeFound(String filter) throws Exception {
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeasureTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeasureType() throws Exception {
        // Get the measureType
        restMeasureTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeasureType() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();

        // Update the measureType
        MeasureType updatedMeasureType = measureTypeRepository.findById(measureType.getId()).get();
        // Disconnect from session so that the updates on updatedMeasureType are not directly saved in db
        em.detach(updatedMeasureType);
        updatedMeasureType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMeasureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeasureType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeasureType))
            )
            .andExpect(status().isOk());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasureType testMeasureType = measureTypeList.get(measureTypeList.size() - 1);
        assertThat(testMeasureType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasureType.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeasureType.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMeasureType.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measureType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measureType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeasureTypeWithPatch() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();

        // Update the measureType using partial update
        MeasureType partialUpdatedMeasureType = new MeasureType();
        partialUpdatedMeasureType.setId(measureType.getId());

        partialUpdatedMeasureType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMeasureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureType))
            )
            .andExpect(status().isOk());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasureType testMeasureType = measureTypeList.get(measureTypeList.size() - 1);
        assertThat(testMeasureType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasureType.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeasureType.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMeasureType.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateMeasureTypeWithPatch() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();

        // Update the measureType using partial update
        MeasureType partialUpdatedMeasureType = new MeasureType();
        partialUpdatedMeasureType.setId(measureType.getId());

        partialUpdatedMeasureType
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMeasureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasureType))
            )
            .andExpect(status().isOk());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
        MeasureType testMeasureType = measureTypeList.get(measureTypeList.size() - 1);
        assertThat(testMeasureType.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMeasureType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasureType.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeasureType.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMeasureType.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measureType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measureType))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasureType() throws Exception {
        int databaseSizeBeforeUpdate = measureTypeRepository.findAll().size();
        measureType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasureTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(measureType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeasureType in the database
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeasureType() throws Exception {
        // Initialize the database
        measureTypeRepository.saveAndFlush(measureType);

        int databaseSizeBeforeDelete = measureTypeRepository.findAll().size();

        // Delete the measureType
        restMeasureTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, measureType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeasureType> measureTypeList = measureTypeRepository.findAll();
        assertThat(measureTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
