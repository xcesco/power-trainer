package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.WorkoutSheet} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.WorkoutSheetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workout-sheets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkoutSheetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkoutType
     */
    public static class WorkoutTypeFilter extends Filter<WorkoutType> {

        public WorkoutTypeFilter() {}

        public WorkoutTypeFilter(WorkoutTypeFilter filter) {
            super(filter);
        }

        @Override
        public WorkoutTypeFilter copy() {
            return new WorkoutTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private StringFilter owner;

    private IntegerFilter prepareTime;

    private IntegerFilter coolDownTime;

    private IntegerFilter cycles;

    private IntegerFilter cycleRestTime;

    private IntegerFilter set;

    private IntegerFilter setRestTime;

    private WorkoutTypeFilter type;

    private LongFilter workoutSheetExerciseId;

    public WorkoutSheetCriteria() {}

    public WorkoutSheetCriteria(WorkoutSheetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.prepareTime = other.prepareTime == null ? null : other.prepareTime.copy();
        this.coolDownTime = other.coolDownTime == null ? null : other.coolDownTime.copy();
        this.cycles = other.cycles == null ? null : other.cycles.copy();
        this.cycleRestTime = other.cycleRestTime == null ? null : other.cycleRestTime.copy();
        this.set = other.set == null ? null : other.set.copy();
        this.setRestTime = other.setRestTime == null ? null : other.setRestTime.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.workoutSheetExerciseId = other.workoutSheetExerciseId == null ? null : other.workoutSheetExerciseId.copy();
    }

    @Override
    public WorkoutSheetCriteria copy() {
        return new WorkoutSheetCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getOwner() {
        return owner;
    }

    public StringFilter owner() {
        if (owner == null) {
            owner = new StringFilter();
        }
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public IntegerFilter getPrepareTime() {
        return prepareTime;
    }

    public IntegerFilter prepareTime() {
        if (prepareTime == null) {
            prepareTime = new IntegerFilter();
        }
        return prepareTime;
    }

    public void setPrepareTime(IntegerFilter prepareTime) {
        this.prepareTime = prepareTime;
    }

    public IntegerFilter getCoolDownTime() {
        return coolDownTime;
    }

    public IntegerFilter coolDownTime() {
        if (coolDownTime == null) {
            coolDownTime = new IntegerFilter();
        }
        return coolDownTime;
    }

    public void setCoolDownTime(IntegerFilter coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public IntegerFilter getCycles() {
        return cycles;
    }

    public IntegerFilter cycles() {
        if (cycles == null) {
            cycles = new IntegerFilter();
        }
        return cycles;
    }

    public void setCycles(IntegerFilter cycles) {
        this.cycles = cycles;
    }

    public IntegerFilter getCycleRestTime() {
        return cycleRestTime;
    }

    public IntegerFilter cycleRestTime() {
        if (cycleRestTime == null) {
            cycleRestTime = new IntegerFilter();
        }
        return cycleRestTime;
    }

    public void setCycleRestTime(IntegerFilter cycleRestTime) {
        this.cycleRestTime = cycleRestTime;
    }

    public IntegerFilter getSet() {
        return set;
    }

    public IntegerFilter set() {
        if (set == null) {
            set = new IntegerFilter();
        }
        return set;
    }

    public void setSet(IntegerFilter set) {
        this.set = set;
    }

    public IntegerFilter getSetRestTime() {
        return setRestTime;
    }

    public IntegerFilter setRestTime() {
        if (setRestTime == null) {
            setRestTime = new IntegerFilter();
        }
        return setRestTime;
    }

    public void setSetRestTime(IntegerFilter setRestTime) {
        this.setRestTime = setRestTime;
    }

    public WorkoutTypeFilter getType() {
        return type;
    }

    public WorkoutTypeFilter type() {
        if (type == null) {
            type = new WorkoutTypeFilter();
        }
        return type;
    }

    public void setType(WorkoutTypeFilter type) {
        this.type = type;
    }

    public LongFilter getWorkoutSheetExerciseId() {
        return workoutSheetExerciseId;
    }

    public LongFilter workoutSheetExerciseId() {
        if (workoutSheetExerciseId == null) {
            workoutSheetExerciseId = new LongFilter();
        }
        return workoutSheetExerciseId;
    }

    public void setWorkoutSheetExerciseId(LongFilter workoutSheetExerciseId) {
        this.workoutSheetExerciseId = workoutSheetExerciseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkoutSheetCriteria that = (WorkoutSheetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(prepareTime, that.prepareTime) &&
            Objects.equals(coolDownTime, that.coolDownTime) &&
            Objects.equals(cycles, that.cycles) &&
            Objects.equals(cycleRestTime, that.cycleRestTime) &&
            Objects.equals(set, that.set) &&
            Objects.equals(setRestTime, that.setRestTime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(workoutSheetExerciseId, that.workoutSheetExerciseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            name,
            owner,
            prepareTime,
            coolDownTime,
            cycles,
            cycleRestTime,
            set,
            setRestTime,
            type,
            workoutSheetExerciseId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (owner != null ? "owner=" + owner + ", " : "") +
            (prepareTime != null ? "prepareTime=" + prepareTime + ", " : "") +
            (coolDownTime != null ? "coolDownTime=" + coolDownTime + ", " : "") +
            (cycles != null ? "cycles=" + cycles + ", " : "") +
            (cycleRestTime != null ? "cycleRestTime=" + cycleRestTime + ", " : "") +
            (set != null ? "set=" + set + ", " : "") +
            (setRestTime != null ? "setRestTime=" + setRestTime + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (workoutSheetExerciseId != null ? "workoutSheetExerciseId=" + workoutSheetExerciseId + ", " : "") +
            "}";
    }
}
