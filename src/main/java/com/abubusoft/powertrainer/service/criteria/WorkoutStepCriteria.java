package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStepType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.WorkoutStep} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.WorkoutStepResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workout-steps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkoutStepCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkoutStepType
     */
    public static class WorkoutStepTypeFilter extends Filter<WorkoutStepType> {

        public WorkoutStepTypeFilter() {}

        public WorkoutStepTypeFilter(WorkoutStepTypeFilter filter) {
            super(filter);
        }

        @Override
        public WorkoutStepTypeFilter copy() {
            return new WorkoutStepTypeFilter(this);
        }
    }

    /**
     * Class for filtering WorkoutStatus
     */
    public static class WorkoutStatusFilter extends Filter<WorkoutStatus> {

        public WorkoutStatusFilter() {}

        public WorkoutStatusFilter(WorkoutStatusFilter filter) {
            super(filter);
        }

        @Override
        public WorkoutStatusFilter copy() {
            return new WorkoutStatusFilter(this);
        }
    }

    /**
     * Class for filtering ValueType
     */
    public static class ValueTypeFilter extends Filter<ValueType> {

        public ValueTypeFilter() {}

        public ValueTypeFilter(ValueTypeFilter filter) {
            super(filter);
        }

        @Override
        public ValueTypeFilter copy() {
            return new ValueTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private IntegerFilter order;

    private IntegerFilter executionTime;

    private WorkoutStepTypeFilter type;

    private WorkoutStatusFilter status;

    private UUIDFilter exerciseUuid;

    private StringFilter exerciseName;

    private IntegerFilter exerciseValue;

    private ValueTypeFilter exerciseValueType;

    private LongFilter workoutId;

    public WorkoutStepCriteria() {}

    public WorkoutStepCriteria(WorkoutStepCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.executionTime = other.executionTime == null ? null : other.executionTime.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.exerciseUuid = other.exerciseUuid == null ? null : other.exerciseUuid.copy();
        this.exerciseName = other.exerciseName == null ? null : other.exerciseName.copy();
        this.exerciseValue = other.exerciseValue == null ? null : other.exerciseValue.copy();
        this.exerciseValueType = other.exerciseValueType == null ? null : other.exerciseValueType.copy();
        this.workoutId = other.workoutId == null ? null : other.workoutId.copy();
    }

    @Override
    public WorkoutStepCriteria copy() {
        return new WorkoutStepCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            uuid = new UUIDFilter();
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public IntegerFilter getExecutionTime() {
        return executionTime;
    }

    public IntegerFilter executionTime() {
        if (executionTime == null) {
            executionTime = new IntegerFilter();
        }
        return executionTime;
    }

    public void setExecutionTime(IntegerFilter executionTime) {
        this.executionTime = executionTime;
    }

    public WorkoutStepTypeFilter getType() {
        return type;
    }

    public WorkoutStepTypeFilter type() {
        if (type == null) {
            type = new WorkoutStepTypeFilter();
        }
        return type;
    }

    public void setType(WorkoutStepTypeFilter type) {
        this.type = type;
    }

    public WorkoutStatusFilter getStatus() {
        return status;
    }

    public WorkoutStatusFilter status() {
        if (status == null) {
            status = new WorkoutStatusFilter();
        }
        return status;
    }

    public void setStatus(WorkoutStatusFilter status) {
        this.status = status;
    }

    public UUIDFilter getExerciseUuid() {
        return exerciseUuid;
    }

    public UUIDFilter exerciseUuid() {
        if (exerciseUuid == null) {
            exerciseUuid = new UUIDFilter();
        }
        return exerciseUuid;
    }

    public void setExerciseUuid(UUIDFilter exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
    }

    public StringFilter getExerciseName() {
        return exerciseName;
    }

    public StringFilter exerciseName() {
        if (exerciseName == null) {
            exerciseName = new StringFilter();
        }
        return exerciseName;
    }

    public void setExerciseName(StringFilter exerciseName) {
        this.exerciseName = exerciseName;
    }

    public IntegerFilter getExerciseValue() {
        return exerciseValue;
    }

    public IntegerFilter exerciseValue() {
        if (exerciseValue == null) {
            exerciseValue = new IntegerFilter();
        }
        return exerciseValue;
    }

    public void setExerciseValue(IntegerFilter exerciseValue) {
        this.exerciseValue = exerciseValue;
    }

    public ValueTypeFilter getExerciseValueType() {
        return exerciseValueType;
    }

    public ValueTypeFilter exerciseValueType() {
        if (exerciseValueType == null) {
            exerciseValueType = new ValueTypeFilter();
        }
        return exerciseValueType;
    }

    public void setExerciseValueType(ValueTypeFilter exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
    }

    public LongFilter getWorkoutId() {
        return workoutId;
    }

    public LongFilter workoutId() {
        if (workoutId == null) {
            workoutId = new LongFilter();
        }
        return workoutId;
    }

    public void setWorkoutId(LongFilter workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkoutStepCriteria that = (WorkoutStepCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(order, that.order) &&
            Objects.equals(executionTime, that.executionTime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(exerciseUuid, that.exerciseUuid) &&
            Objects.equals(exerciseName, that.exerciseName) &&
            Objects.equals(exerciseValue, that.exerciseValue) &&
            Objects.equals(exerciseValueType, that.exerciseValueType) &&
            Objects.equals(workoutId, that.workoutId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            order,
            executionTime,
            type,
            status,
            exerciseUuid,
            exerciseName,
            exerciseValue,
            exerciseValueType,
            workoutId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStepCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (executionTime != null ? "executionTime=" + executionTime + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (exerciseUuid != null ? "exerciseUuid=" + exerciseUuid + ", " : "") +
            (exerciseName != null ? "exerciseName=" + exerciseName + ", " : "") +
            (exerciseValue != null ? "exerciseValue=" + exerciseValue + ", " : "") +
            (exerciseValueType != null ? "exerciseValueType=" + exerciseValueType + ", " : "") +
            (workoutId != null ? "workoutId=" + workoutId + ", " : "") +
            "}";
    }
}
