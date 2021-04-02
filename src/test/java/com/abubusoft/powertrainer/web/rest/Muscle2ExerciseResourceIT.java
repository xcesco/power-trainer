package com.abubusoft.powertrainer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abubusoft.powertrainer.IntegrationTest;
import com.abubusoft.powertrainer.domain.Muscle2Exercise;
import com.abubusoft.powertrainer.repository.Muscle2ExerciseRepository;
import com.abubusoft.powertrainer.service.criteria.Muscle2ExerciseCriteria;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link Muscle2ExerciseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Muscle2ExerciseResourceIT {

    private static final String ENTITY_API_URL = "/api/muscle-2-exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Muscle2ExerciseRepository muscle2ExerciseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMuscle2ExerciseMockMvc;

    private Muscle2Exercise muscle2Exercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle2Exercise createEntity(EntityManager em) {
        Muscle2Exercise muscle2Exercise = new Muscle2Exercise();
        return muscle2Exercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Muscle2Exercise createUpdatedEntity(EntityManager em) {
        Muscle2Exercise muscle2Exercise = new Muscle2Exercise();
        return muscle2Exercise;
    }

    @BeforeEach
    public void initTest() {
        muscle2Exercise = createEntity(em);
    }

    @Test
    @Transactional
    void createMuscle2Exercise() throws Exception {
        int databaseSizeBeforeCreate = muscle2ExerciseRepository.findAll().size();
        // Create the Muscle2Exercise
        restMuscle2ExerciseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isCreated());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Muscle2Exercise testMuscle2Exercise = muscle2ExerciseList.get(muscle2ExerciseList.size() - 1);
    }

    @Test
    @Transactional
    void createMuscle2ExerciseWithExistingId() throws Exception {
        // Create the Muscle2Exercise with an existing ID
        muscle2Exercise.setId(1L);

        int databaseSizeBeforeCreate = muscle2ExerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuscle2ExerciseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMuscle2Exercises() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        // Get all the muscle2ExerciseList
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle2Exercise.getId().intValue())));
    }

    @Test
    @Transactional
    void getMuscle2Exercise() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        // Get the muscle2Exercise
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, muscle2Exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(muscle2Exercise.getId().intValue()));
    }

    @Test
    @Transactional
    void getMuscle2ExercisesByIdFiltering() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        Long id = muscle2Exercise.getId();

        defaultMuscle2ExerciseShouldBeFound("id.equals=" + id);
        defaultMuscle2ExerciseShouldNotBeFound("id.notEquals=" + id);

        defaultMuscle2ExerciseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMuscle2ExerciseShouldNotBeFound("id.greaterThan=" + id);

        defaultMuscle2ExerciseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMuscle2ExerciseShouldNotBeFound("id.lessThan=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMuscle2ExerciseShouldBeFound(String filter) throws Exception {
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(muscle2Exercise.getId().intValue())));

        // Check, that the count call also returns 1
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMuscle2ExerciseShouldNotBeFound(String filter) throws Exception {
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMuscle2ExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMuscle2Exercise() throws Exception {
        // Get the muscle2Exercise
        restMuscle2ExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMuscle2Exercise() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();

        // Update the muscle2Exercise
        Muscle2Exercise updatedMuscle2Exercise = muscle2ExerciseRepository.findById(muscle2Exercise.getId()).get();
        // Disconnect from session so that the updates on updatedMuscle2Exercise are not directly saved in db
        em.detach(updatedMuscle2Exercise);

        restMuscle2ExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMuscle2Exercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMuscle2Exercise))
            )
            .andExpect(status().isOk());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
        Muscle2Exercise testMuscle2Exercise = muscle2ExerciseList.get(muscle2ExerciseList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, muscle2Exercise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMuscle2ExerciseWithPatch() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();

        // Update the muscle2Exercise using partial update
        Muscle2Exercise partialUpdatedMuscle2Exercise = new Muscle2Exercise();
        partialUpdatedMuscle2Exercise.setId(muscle2Exercise.getId());

        restMuscle2ExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle2Exercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle2Exercise))
            )
            .andExpect(status().isOk());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
        Muscle2Exercise testMuscle2Exercise = muscle2ExerciseList.get(muscle2ExerciseList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMuscle2ExerciseWithPatch() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();

        // Update the muscle2Exercise using partial update
        Muscle2Exercise partialUpdatedMuscle2Exercise = new Muscle2Exercise();
        partialUpdatedMuscle2Exercise.setId(muscle2Exercise.getId());

        restMuscle2ExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMuscle2Exercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMuscle2Exercise))
            )
            .andExpect(status().isOk());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
        Muscle2Exercise testMuscle2Exercise = muscle2ExerciseList.get(muscle2ExerciseList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, muscle2Exercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMuscle2Exercise() throws Exception {
        int databaseSizeBeforeUpdate = muscle2ExerciseRepository.findAll().size();
        muscle2Exercise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMuscle2ExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(muscle2Exercise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Muscle2Exercise in the database
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMuscle2Exercise() throws Exception {
        // Initialize the database
        muscle2ExerciseRepository.saveAndFlush(muscle2Exercise);

        int databaseSizeBeforeDelete = muscle2ExerciseRepository.findAll().size();

        // Delete the muscle2Exercise
        restMuscle2ExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, muscle2Exercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Muscle2Exercise> muscle2ExerciseList = muscle2ExerciseRepository.findAll();
        assertThat(muscle2ExerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
