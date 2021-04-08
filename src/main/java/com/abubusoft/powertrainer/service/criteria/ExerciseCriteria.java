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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Exercise} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.ExerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExerciseCriteria implements Serializable, Criteria {

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

    private StringFilter name;

    private ValueTypeFilter valueType;

    private StringFilter owner;

    private LongFilter exerciseToolId;

    private LongFilter noteId;

    private LongFilter muscleId;

    public ExerciseCriteria() {}

    public ExerciseCriteria(ExerciseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.valueType = other.valueType == null ? null : other.valueType.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.exerciseToolId = other.exerciseToolId == null ? null : other.exerciseToolId.copy();
        this.noteId = other.noteId == null ? null : other.noteId.copy();
        this.muscleId = other.muscleId == null ? null : other.muscleId.copy();
    }

    @Override
    public ExerciseCriteria copy() {
        return new ExerciseCriteria(this);
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

    public ValueTypeFilter getValueType() {
        return valueType;
    }

    public ValueTypeFilter valueType() {
        if (valueType == null) {
            valueType = new ValueTypeFilter();
        }
        return valueType;
    }

    public void setValueType(ValueTypeFilter valueType) {
        this.valueType = valueType;
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

    public LongFilter getExerciseToolId() {
        return exerciseToolId;
    }

    public LongFilter exerciseToolId() {
        if (exerciseToolId == null) {
            exerciseToolId = new LongFilter();
        }
        return exerciseToolId;
    }

    public void setExerciseToolId(LongFilter exerciseToolId) {
        this.exerciseToolId = exerciseToolId;
    }

    public LongFilter getNoteId() {
        return noteId;
    }

    public LongFilter noteId() {
        if (noteId == null) {
            noteId = new LongFilter();
        }
        return noteId;
    }

    public void setNoteId(LongFilter noteId) {
        this.noteId = noteId;
    }

    public LongFilter getMuscleId() {
        return muscleId;
    }

    public LongFilter muscleId() {
        if (muscleId == null) {
            muscleId = new LongFilter();
        }
        return muscleId;
    }

    public void setMuscleId(LongFilter muscleId) {
        this.muscleId = muscleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExerciseCriteria that = (ExerciseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(exerciseToolId, that.exerciseToolId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(muscleId, that.muscleId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, valueType, owner, exerciseToolId, noteId, muscleId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (valueType != null ? "valueType=" + valueType + ", " : "") +
            (owner != null ? "owner=" + owner + ", " : "") +
            (exerciseToolId != null ? "exerciseToolId=" + exerciseToolId + ", " : "") +
            (noteId != null ? "noteId=" + noteId + ", " : "") +
            (muscleId != null ? "muscleId=" + muscleId + ", " : "") +
            "}";
    }
}