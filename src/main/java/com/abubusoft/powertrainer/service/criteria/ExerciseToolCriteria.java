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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.ExerciseTool} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.ExerciseToolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exercise-tools?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExerciseToolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private LongFilter exerciseId;

    public ExerciseToolCriteria() {}

    public ExerciseToolCriteria(ExerciseToolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.exerciseId = other.exerciseId == null ? null : other.exerciseId.copy();
    }

    @Override
    public ExerciseToolCriteria copy() {
        return new ExerciseToolCriteria(this);
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

    public LongFilter getExerciseId() {
        return exerciseId;
    }

    public LongFilter exerciseId() {
        if (exerciseId == null) {
            exerciseId = new LongFilter();
        }
        return exerciseId;
    }

    public void setExerciseId(LongFilter exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExerciseToolCriteria that = (ExerciseToolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(exerciseId, that.exerciseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, exerciseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseToolCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (exerciseId != null ? "exerciseId=" + exerciseId + ", " : "") +
            "}";
    }
}
