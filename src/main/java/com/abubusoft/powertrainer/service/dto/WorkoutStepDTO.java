package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStepType;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.WorkoutStep} entity.
 */
public class WorkoutStepDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    private Integer order;

    private Integer executionTime;

    private WorkoutStepType type;

    private WorkoutStatus status;

    @NotNull
    private UUID exerciseUuid;

    @NotNull
    private String exerciseName;

    @NotNull
    private Integer exerciseValue;

    @NotNull
    private ValueType exerciseValueType;

    private WorkoutDTO workout;

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

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public WorkoutStepType getType() {
        return type;
    }

    public void setType(WorkoutStepType type) {
        this.type = type;
    }

    public WorkoutStatus getStatus() {
        return status;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
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

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutStepDTO)) {
            return false;
        }

        WorkoutStepDTO workoutStepDTO = (WorkoutStepDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutStepDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStepDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", executionTime=" + getExecutionTime() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", exerciseUuid='" + getExerciseUuid() + "'" +
            ", exerciseName='" + getExerciseName() + "'" +
            ", exerciseValue=" + getExerciseValue() +
            ", exerciseValueType='" + getExerciseValueType() + "'" +
            ", workout=" + getWorkout() +
            "}";
    }
}
