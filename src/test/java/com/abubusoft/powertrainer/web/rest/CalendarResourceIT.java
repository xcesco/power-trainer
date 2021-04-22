package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Calendar;
import com.abubusoft.powertrainer.domain.ExerciseValue;
import com.abubusoft.powertrainer.domain.Misuration;
import com.abubusoft.powertrainer.domain.Workout;
import com.abubusoft.powertrainer.repository.CalendarRepository;
import com.abubusoft.powertrainer.service.criteria.CalendarCriteria;
import com.abubusoft.powertrainer.service.dto.CalendarDTO;
import com.abubusoft.powertrainer.service.mapper.CalendarMapper;
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
 * Integration tests for the {@link CalendarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendarResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendarMockMvc;

    private Calendar calendar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createEntity(EntityManager em) {
        Calendar calendar = new Calendar().uuid(DEFAULT_UUID).name(DEFAULT_NAME).owner(DEFAULT_OWNER);
        return calendar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createUpdatedEntity(EntityManager em) {
        Calendar calendar = new Calendar().uuid(UPDATED_UUID).name(UPDATED_NAME).owner(UPDATED_OWNER);
        return calendar;
    }

    @BeforeEach
    public void initTest() {
        calendar = createEntity(em);
    }

    @Test
    @Transactional
    void createCalendar() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();
        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);
        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isCreated());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate + 1);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCalendar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCalendar.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    void createCalendarWithExistingId() throws Exception {
        // Create the Calendar with an existing ID
        calendar.setId(1L);
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarRepository.findAll().size();
        // set the field null
        calendar.setUuid(null);

        // Create the Calendar, which fails.
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarRepository.findAll().size();
        // set the field null
        calendar.setName(null);

        // Create the Calendar, which fails.
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarRepository.findAll().size();
        // set the field null
        calendar.setOwner(null);

        // Create the Calendar, which fails.
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isBadRequest());

        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendars() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }

    @Test
    @Transactional
    void getCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get the calendar
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL_ID, calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendar.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER));
    }

    @Test
    @Transactional
    void getCalendarsByIdFiltering() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        Long id = calendar.getId();

        defaultCalendarShouldBeFound("id.equals=" + id);
        defaultCalendarShouldNotBeFound("id.notEquals=" + id);

        defaultCalendarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCalendarShouldNotBeFound("id.greaterThan=" + id);

        defaultCalendarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCalendarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCalendarsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uuid equals to DEFAULT_UUID
        defaultCalendarShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the calendarList where uuid equals to UPDATED_UUID
        defaultCalendarShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCalendarsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uuid not equals to DEFAULT_UUID
        defaultCalendarShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the calendarList where uuid not equals to UPDATED_UUID
        defaultCalendarShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCalendarsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultCalendarShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the calendarList where uuid equals to UPDATED_UUID
        defaultCalendarShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCalendarsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where uuid is not null
        defaultCalendarShouldBeFound("uuid.specified=true");

        // Get all the calendarList where uuid is null
        defaultCalendarShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name equals to DEFAULT_NAME
        defaultCalendarShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the calendarList where name equals to UPDATED_NAME
        defaultCalendarShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCalendarsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name not equals to DEFAULT_NAME
        defaultCalendarShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the calendarList where name not equals to UPDATED_NAME
        defaultCalendarShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCalendarsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCalendarShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the calendarList where name equals to UPDATED_NAME
        defaultCalendarShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCalendarsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name is not null
        defaultCalendarShouldBeFound("name.specified=true");

        // Get all the calendarList where name is null
        defaultCalendarShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarsByNameContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name contains DEFAULT_NAME
        defaultCalendarShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the calendarList where name contains UPDATED_NAME
        defaultCalendarShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCalendarsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where name does not contain DEFAULT_NAME
        defaultCalendarShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the calendarList where name does not contain UPDATED_NAME
        defaultCalendarShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner equals to DEFAULT_OWNER
        defaultCalendarShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the calendarList where owner equals to UPDATED_OWNER
        defaultCalendarShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner not equals to DEFAULT_OWNER
        defaultCalendarShouldNotBeFound("owner.notEquals=" + DEFAULT_OWNER);

        // Get all the calendarList where owner not equals to UPDATED_OWNER
        defaultCalendarShouldBeFound("owner.notEquals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultCalendarShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the calendarList where owner equals to UPDATED_OWNER
        defaultCalendarShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner is not null
        defaultCalendarShouldBeFound("owner.specified=true");

        // Get all the calendarList where owner is null
        defaultCalendarShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner contains DEFAULT_OWNER
        defaultCalendarShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the calendarList where owner contains UPDATED_OWNER
        defaultCalendarShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCalendarsByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        // Get all the calendarList where owner does not contain DEFAULT_OWNER
        defaultCalendarShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the calendarList where owner does not contain UPDATED_OWNER
        defaultCalendarShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCalendarsByExerciseValueIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        ExerciseValue exerciseValue = ExerciseValueResourceIT.createEntity(em);
        em.persist(exerciseValue);
        em.flush();
        calendar.addExerciseValue(exerciseValue);
        calendarRepository.saveAndFlush(calendar);
        Long exerciseValueId = exerciseValue.getId();

        // Get all the calendarList where exerciseValue equals to exerciseValueId
        defaultCalendarShouldBeFound("exerciseValueId.equals=" + exerciseValueId);

        // Get all the calendarList where exerciseValue equals to (exerciseValueId + 1)
        defaultCalendarShouldNotBeFound("exerciseValueId.equals=" + (exerciseValueId + 1));
    }

    @Test
    @Transactional
    void getAllCalendarsByMisurationIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        Misuration misuration = MisurationResourceIT.createEntity(em);
        em.persist(misuration);
        em.flush();
        calendar.addMisuration(misuration);
        calendarRepository.saveAndFlush(calendar);
        Long misurationId = misuration.getId();

        // Get all the calendarList where misuration equals to misurationId
        defaultCalendarShouldBeFound("misurationId.equals=" + misurationId);

        // Get all the calendarList where misuration equals to (misurationId + 1)
        defaultCalendarShouldNotBeFound("misurationId.equals=" + (misurationId + 1));
    }

    @Test
    @Transactional
    void getAllCalendarsByWorkoutIsEqualToSomething() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);
        Workout workout = WorkoutResourceIT.createEntity(em);
        em.persist(workout);
        em.flush();
        calendar.addWorkout(workout);
        calendarRepository.saveAndFlush(calendar);
        Long workoutId = workout.getId();

        // Get all the calendarList where workout equals to workoutId
        defaultCalendarShouldBeFound("workoutId.equals=" + workoutId);

        // Get all the calendarList where workout equals to (workoutId + 1)
        defaultCalendarShouldNotBeFound("workoutId.equals=" + (workoutId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCalendarShouldBeFound(String filter) throws Exception {
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));

        // Check, that the count call also returns 1
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCalendarShouldNotBeFound(String filter) throws Exception {
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCalendar() throws Exception {
        // Get the calendar
        restCalendarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar
        Calendar updatedCalendar = calendarRepository.findById(calendar.getId()).get();
        // Disconnect from session so that the updates on updatedCalendar are not directly saved in db
        em.detach(updatedCalendar);
        updatedCalendar.uuid(UPDATED_UUID).name(UPDATED_NAME).owner(UPDATED_OWNER);
        CalendarDTO calendarDTO = calendarMapper.toDto(updatedCalendar);

        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCalendar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCalendar.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void putNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendarDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCalendar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCalendar.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    void fullUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        partialUpdatedCalendar.uuid(UPDATED_UUID).name(UPDATED_NAME).owner(UPDATED_OWNER);

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCalendar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCalendar.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    void patchNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendarDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(count.incrementAndGet());

        // Create the Calendar
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calendarDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendar() throws Exception {
        // Initialize the database
        calendarRepository.saveAndFlush(calendar);

        int databaseSizeBeforeDelete = calendarRepository.findAll().size();

        // Delete the calendar
        restCalendarMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
