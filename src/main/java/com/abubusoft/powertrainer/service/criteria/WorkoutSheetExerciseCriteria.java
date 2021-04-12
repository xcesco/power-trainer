package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.WorkoutSheetExercise} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.WorkoutSheetExerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workout-sheet-exercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkoutSheetExerciseCriteria implements Serializable, Criteria {

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

    private IntegerFilter repetitions;

    private UUIDFilter exerciseUuid;

    private StringFilter exerciseName;

    private IntegerFilter exerciseValue;

    private ValueTypeFilter exerciseValueType;

    private LongFilter workoutSheetId;

    public WorkoutSheetExerciseCriteria() {}

    public WorkoutSheetExerciseCriteria(WorkoutSheetExerciseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.repetitions = other.repetitions == null ? null : other.repetitions.copy();
        this.exerciseUuid = other.exerciseUuid == null ? null : other.exerciseUuid.copy();
        this.exerciseName = other.exerciseName == null ? null : other.exerciseName.copy();
        this.exerciseValue = other.exerciseValue == null ? null : other.exerciseValue.copy();
        this.exerciseValueType = other.exerciseValueType == null ? null : other.exerciseValueType.copy();
        this.workoutSheetId = other.workoutSheetId == null ? null : other.workoutSheetId.copy();
    }

    @Override
    public WorkoutSheetExerciseCriteria copy() {
        return new WorkoutSheetExerciseCriteria(this);
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

    public IntegerFilter getRepetitions() {
        return repetitions;
    }

    public IntegerFilter repetitions() {
        if (repetitions == null) {
            repetitions = new IntegerFilter();
        }
        return repetitions;
    }

    public void setRepetitions(IntegerFilter repetitions) {
        this.repetitions = repetitions;
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

    public LongFilter getWorkoutSheetId() {
        return workoutSheetId;
    }

    public LongFilter workoutSheetId() {
        if (workoutSheetId == null) {
            workoutSheetId = new LongFilter();
        }
        return workoutSheetId;
    }

    public void setWorkoutSheetId(LongFilter workoutSheetId) {
        this.workoutSheetId = workoutSheetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkoutSheetExerciseCriteria that = (WorkoutSheetExerciseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(order, that.order) &&
            Objects.equals(repetitions, that.repetitions) &&
            Objects.equals(exerciseUuid, that.exerciseUuid) &&
            Objects.equals(exerciseName, that.exerciseName) &&
            Objects.equals(exerciseValue, that.exerciseValue) &&
            Objects.equals(exerciseValueType, that.exerciseValueType) &&
            Objects.equals(workoutSheetId, that.workoutSheetId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, order, repetitions, exerciseUuid, exerciseName, exerciseValue, exerciseValueType, workoutSheetId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheetExerciseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (repetitions != null ? "repetitions=" + repetitions + ", " : "") +
            (exerciseUuid != null ? "exerciseUuid=" + exerciseUuid + ", " : "") +
            (exerciseName != null ? "exerciseName=" + exerciseName + ", " : "") +
            (exerciseValue != null ? "exerciseValue=" + exerciseValue + ", " : "") +
            (exerciseValueType != null ? "exerciseValueType=" + exerciseValueType + ", " : "") +
            (workoutSheetId != null ? "workoutSheetId=" + workoutSheetId + ", " : "") +
            "}";
    }
}
