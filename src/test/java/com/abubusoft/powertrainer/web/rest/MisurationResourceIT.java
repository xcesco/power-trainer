package com.abubusoft.powertrainer.web.rest;

import static com.abubusoft.powertrainer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.domain.MisurationType;
import com.abubusoft.powertrainer.repository.MisurationRepository;
import com.abubusoft.powertrainer.service.criteria.MisurationCriteria;
import com.abubusoft.powertrainer.service.dto.MisurationDTO;
import com.abubusoft.powertrainer.service.mapper.MisurationMapper;
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
 * Integration tests for the {@link MisurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MisurationResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;
    private static final Integer SMALLER_VALUE = 1 - 1;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/misurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MisurationRepository misurationRepository;

    @Autowired
    private MisurationMapper misurationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMisurationMockMvc;

    private Misuration misuration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Misuration createEntity(EntityManager em) {
        Misuration misuration = new Misuration()
            .uuid(DEFAULT_UUID)
            .date(DEFAULT_DATE)
            .value(DEFAULT_VALUE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .note(DEFAULT_NOTE);
        return misuration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Misuration createUpdatedEntity(EntityManager em) {
        Misuration misuration = new Misuration()
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);
        return misuration;
    }

    @BeforeEach
    public void initTest() {
        misuration = createEntity(em);
    }

    @Test
    @Transactional
    void createMisuration() throws Exception {
        int databaseSizeBeforeCreate = misurationRepository.findAll().size();
        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);
        restMisurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isCreated());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeCreate + 1);
        Misuration testMisuration = misurationList.get(misurationList.size() - 1);
        assertThat(testMisuration.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMisuration.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMisuration.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMisuration.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMisuration.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMisuration.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createMisurationWithExistingId() throws Exception {
        // Create the Misuration with an existing ID
        misuration.setId(1L);
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        int databaseSizeBeforeCreate = misurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMisurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = misurationRepository.findAll().size();
        // set the field null
        misuration.setUuid(null);

        // Create the Misuration, which fails.
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        restMisurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isBadRequest());

        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = misurationRepository.findAll().size();
        // set the field null
        misuration.setDate(null);

        // Create the Misuration, which fails.
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        restMisurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isBadRequest());

        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = misurationRepository.findAll().size();
        // set the field null
        misuration.setValue(null);

        // Create the Misuration, which fails.
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        restMisurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isBadRequest());

        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMisurations() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    void getMisuration() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get the misuration
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL_ID, misuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(misuration.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    void getMisurationsByIdFiltering() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        Long id = misuration.getId();

        defaultMisurationShouldBeFound("id.equals=" + id);
        defaultMisurationShouldNotBeFound("id.notEquals=" + id);

        defaultMisurationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMisurationShouldNotBeFound("id.greaterThan=" + id);

        defaultMisurationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMisurationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMisurationsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where uuid equals to DEFAULT_UUID
        defaultMisurationShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the misurationList where uuid equals to UPDATED_UUID
        defaultMisurationShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where uuid not equals to DEFAULT_UUID
        defaultMisurationShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the misurationList where uuid not equals to UPDATED_UUID
        defaultMisurationShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultMisurationShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the misurationList where uuid equals to UPDATED_UUID
        defaultMisurationShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMisurationsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where uuid is not null
        defaultMisurationShouldBeFound("uuid.specified=true");

        // Get all the misurationList where uuid is null
        defaultMisurationShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date equals to DEFAULT_DATE
        defaultMisurationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the misurationList where date equals to UPDATED_DATE
        defaultMisurationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date not equals to DEFAULT_DATE
        defaultMisurationShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the misurationList where date not equals to UPDATED_DATE
        defaultMisurationShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMisurationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the misurationList where date equals to UPDATED_DATE
        defaultMisurationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date is not null
        defaultMisurationShouldBeFound("date.specified=true");

        // Get all the misurationList where date is null
        defaultMisurationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date is greater than or equal to DEFAULT_DATE
        defaultMisurationShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the misurationList where date is greater than or equal to UPDATED_DATE
        defaultMisurationShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date is less than or equal to DEFAULT_DATE
        defaultMisurationShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the misurationList where date is less than or equal to SMALLER_DATE
        defaultMisurationShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date is less than DEFAULT_DATE
        defaultMisurationShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the misurationList where date is less than UPDATED_DATE
        defaultMisurationShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where date is greater than DEFAULT_DATE
        defaultMisurationShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the misurationList where date is greater than SMALLER_DATE
        defaultMisurationShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value equals to DEFAULT_VALUE
        defaultMisurationShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the misurationList where value equals to UPDATED_VALUE
        defaultMisurationShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value not equals to DEFAULT_VALUE
        defaultMisurationShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the misurationList where value not equals to UPDATED_VALUE
        defaultMisurationShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultMisurationShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the misurationList where value equals to UPDATED_VALUE
        defaultMisurationShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value is not null
        defaultMisurationShouldBeFound("value.specified=true");

        // Get all the misurationList where value is null
        defaultMisurationShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value is greater than or equal to DEFAULT_VALUE
        defaultMisurationShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the misurationList where value is greater than or equal to UPDATED_VALUE
        defaultMisurationShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value is less than or equal to DEFAULT_VALUE
        defaultMisurationShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the misurationList where value is less than or equal to SMALLER_VALUE
        defaultMisurationShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value is less than DEFAULT_VALUE
        defaultMisurationShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the misurationList where value is less than UPDATED_VALUE
        defaultMisurationShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        // Get all the misurationList where value is greater than DEFAULT_VALUE
        defaultMisurationShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the misurationList where value is greater than SMALLER_VALUE
        defaultMisurationShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllMisurationsByCalendarIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);
        Calendar calendar = CalendarResourceIT.createEntity(em);
        em.persist(calendar);
        em.flush();
        misuration.setCalendar(calendar);
        misurationRepository.saveAndFlush(misuration);
        Long calendarId = calendar.getId();

        // Get all the misurationList where calendar equals to calendarId
        defaultMisurationShouldBeFound("calendarId.equals=" + calendarId);

        // Get all the misurationList where calendar equals to (calendarId + 1)
        defaultMisurationShouldNotBeFound("calendarId.equals=" + (calendarId + 1));
    }

    @Test
    @Transactional
    void getAllMisurationsByMisurationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);
        MisurationType misurationType = MisurationTypeResourceIT.createEntity(em);
        em.persist(misurationType);
        em.flush();
        misuration.setMisurationType(misurationType);
        misurationRepository.saveAndFlush(misuration);
        Long misurationTypeId = misurationType.getId();

        // Get all the misurationList where misurationType equals to misurationTypeId
        defaultMisurationShouldBeFound("misurationTypeId.equals=" + misurationTypeId);

        // Get all the misurationList where misurationType equals to (misurationTypeId + 1)
        defaultMisurationShouldNotBeFound("misurationTypeId.equals=" + (misurationTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMisurationShouldBeFound(String filter) throws Exception {
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));

        // Check, that the count call also returns 1
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMisurationShouldNotBeFound(String filter) throws Exception {
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMisurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMisuration() throws Exception {
        // Get the misuration
        restMisurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMisuration() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();

        // Update the misuration
        Misuration updatedMisuration = misurationRepository.findById(misuration.getId()).get();
        // Disconnect from session so that the updates on updatedMisuration are not directly saved in db
        em.detach(updatedMisuration);
        updatedMisuration
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);
        MisurationDTO misurationDTO = misurationMapper.toDto(updatedMisuration);

        restMisurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
        Misuration testMisuration = misurationList.get(misurationList.size() - 1);
        assertThat(testMisuration.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMisuration.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMisuration.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMisuration.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMisuration.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMisuration.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMisurationWithPatch() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();

        // Update the misuration using partial update
        Misuration partialUpdatedMisuration = new Misuration();
        partialUpdatedMisuration.setId(misuration.getId());

        restMisurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisuration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisuration))
            )
            .andExpect(status().isOk());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
        Misuration testMisuration = misurationList.get(misurationList.size() - 1);
        assertThat(testMisuration.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testMisuration.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMisuration.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testMisuration.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMisuration.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMisuration.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateMisurationWithPatch() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();

        // Update the misuration using partial update
        Misuration partialUpdatedMisuration = new Misuration();
        partialUpdatedMisuration.setId(misuration.getId());

        partialUpdatedMisuration
            .uuid(UPDATED_UUID)
            .date(UPDATED_DATE)
            .value(UPDATED_VALUE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .note(UPDATED_NOTE);

        restMisurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisuration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisuration))
            )
            .andExpect(status().isOk());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
        Misuration testMisuration = misurationList.get(misurationList.size() - 1);
        assertThat(testMisuration.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testMisuration.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMisuration.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testMisuration.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMisuration.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMisuration.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, misurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMisuration() throws Exception {
        int databaseSizeBeforeUpdate = misurationRepository.findAll().size();
        misuration.setId(count.incrementAndGet());

        // Create the Misuration
        MisurationDTO misurationDTO = misurationMapper.toDto(misuration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisurationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(misurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Misuration in the database
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMisuration() throws Exception {
        // Initialize the database
        misurationRepository.saveAndFlush(misuration);

        int databaseSizeBeforeDelete = misurationRepository.findAll().size();

        // Delete the misuration
        restMisurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, misuration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Misuration> misurationList = misurationRepository.findAll();
        assertThat(misurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
