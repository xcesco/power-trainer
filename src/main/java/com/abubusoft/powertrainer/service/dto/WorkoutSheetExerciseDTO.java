package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.WorkoutSheetExercise} entity.
 */
public class WorkoutSheetExerciseDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    private Integer order;

    private Integer repetitions;

    @NotNull
    private UUID exerciseUuid;

    @NotNull
    private String exerciseName;

    @NotNull
    private Integer exerciseValue;

    @NotNull
    private ValueType exerciseValueType;

    private WorkoutSheetDTO workoutSheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public UUID getExerciseUuid() {
        return exerciseUuid;
    }

    public void setExerciseUuid(UUID exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getExerciseValue() {
        return exerciseValue;
    }

    public void setExerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
    }

    public ValueType getExerciseValueType() {
        return exerciseValueType;
    }

    public void setExerciseValueType(ValueType exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
    }

    public WorkoutSheetDTO getWorkoutSheet() {
        return workoutSheet;
    }

    public void setWorkoutSheet(WorkoutSheetDTO workoutSheet) {
        this.workoutSheet = workoutSheet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutSheetExerciseDTO)) {
            return false;
        }

        WorkoutSheetExerciseDTO workoutSheetExerciseDTO = (WorkoutSheetExerciseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutSheetExerciseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheetExerciseDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", repetitions=" + getRepetitions() +
            ", exerciseUuid='" + getExerciseUuid() + "'" +
            ", exerciseName='" + getExerciseName() + "'" +
            ", exerciseValue=" + getExerciseValue() +
            ", exerciseValueType='" + getExerciseValueType() + "'" +
            ", workoutSheet=" + getWorkoutSheet() +
            "}";
    }
}
