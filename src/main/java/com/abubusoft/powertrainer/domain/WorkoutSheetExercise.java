package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutSheetExercise.
 */
@Entity
@Table(name = "workout_sheet_exercises")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkoutSheetExercise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "repetitions")
    private Integer repetitions;

    @NotNull
    @Column(name = "exercise_uuid", nullable = false)
    private UUID exerciseUuid;

    @NotNull
    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @NotNull
    @Column(name = "exercise_value", nullable = false)
    private Integer exerciseValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_value_type", nullable = false)
    private ValueType exerciseValueType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workoutSheetExercises" }, allowSetters = true)
    private WorkoutSheet workoutSheet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutSheetExercise id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public WorkoutSheetExercise uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getOrder() {
        return this.order;
    }

    public WorkoutSheetExercise order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getRepetitions() {
        return this.repetitions;
    }

    public WorkoutSheetExercise repetitions(Integer repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public UUID getExerciseUuid() {
        return this.exerciseUuid;
    }

    public WorkoutSheetExercise exerciseUuid(UUID exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
        return this;
    }

    public void setExerciseUuid(UUID exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public WorkoutSheetExercise exerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
        return this;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getExerciseValue() {
        return this.exerciseValue;
    }

    public WorkoutSheetExercise exerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
        return this;
    }

    public void setExerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
    }

    public ValueType getExerciseValueType() {
        return this.exerciseValueType;
    }

    public WorkoutSheetExercise exerciseValueType(ValueType exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
        return this;
    }

    public void setExerciseValueType(ValueType exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
    }

    public WorkoutSheet getWorkoutSheet() {
        return this.workoutSheet;
    }

    public WorkoutSheetExercise workoutSheet(WorkoutSheet workoutSheet) {
        this.setWorkoutSheet(workoutSheet);
        return this;
    }

    public void setWorkoutSheet(WorkoutSheet workoutSheet) {
        this.workoutSheet = workoutSheet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutSheetExercise)) {
            return false;
        }
        return id != null && id.equals(((WorkoutSheetExercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheetExercise{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", repetitions=" + getRepetitions() +
            ", exerciseUuid='" + getExerciseUuid() + "'" +
            ", exerciseName='" + getExerciseName() + "'" +
            ", exerciseValue=" + getExerciseValue() +
            ", exerciseValueType='" + getExerciseValueType() + "'" +
            "}";
    }
}
