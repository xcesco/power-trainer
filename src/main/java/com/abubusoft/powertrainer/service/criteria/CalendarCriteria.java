package com.abubusoft.powertrainer.service.criteria;

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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Calendar} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.CalendarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /calendars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CalendarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private StringFilter owner;

    private LongFilter exerciseValueId;

    private LongFilter misurationId;

    private LongFilter workoutId;

    public CalendarCriteria() {}

    public CalendarCriteria(CalendarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.exerciseValueId = other.exerciseValueId == null ? null : other.exerciseValueId.copy();
        this.misurationId = other.misurationId == null ? null : other.misurationId.copy();
        this.workoutId = other.workoutId == null ? null : other.workoutId.copy();
    }

    @Override
    public CalendarCriteria copy() {
        return new CalendarCriteria(this);
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

    public LongFilter getExerciseValueId() {
        return exerciseValueId;
    }

    public LongFilter exerciseValueId() {
        if (exerciseValueId == null) {
            exerciseValueId = new LongFilter();
        }
        return exerciseValueId;
    }

    public void setExerciseValueId(LongFilter exerciseValueId) {
        this.exerciseValueId = exerciseValueId;
    }

    public LongFilter getMisurationId() {
        return misurationId;
    }

    public LongFilter misurationId() {
        if (misurationId == null) {
            misurationId = new LongFilter();
        }
        return misurationId;
    }

    public void setMisurationId(LongFilter misurationId) {
        this.misurationId = misurationId;
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
        final CalendarCriteria that = (CalendarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(exerciseValueId, that.exerciseValueId) &&
            Objects.equals(misurationId, that.misurationId) &&
            Objects.equals(workoutId, that.workoutId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, owner, exerciseValueId, misurationId, workoutId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalendarCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (owner != null ? "owner=" + owner + ", " : "") +
            (exerciseValueId != null ? "exerciseValueId=" + exerciseValueId + ", " : "") +
            (misurationId != null ? "misurationId=" + misurationId + ", " : "") +
            (workoutId != null ? "workoutId=" + workoutId + ", " : "") +
            "}";
    }
}
