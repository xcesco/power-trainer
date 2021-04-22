package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.ExerciseResourceType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.ExerciseResource} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.ExerciseResourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exercise-resources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExerciseResourceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ExerciseResourceType
     */
    public static class ExerciseResourceTypeFilter extends Filter<ExerciseResourceType> {

        public ExerciseResourceTypeFilter() {}

        public ExerciseResourceTypeFilter(ExerciseResourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public ExerciseResourceTypeFilter copy() {
            return new ExerciseResourceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private IntegerFilter order;

    private ExerciseResourceTypeFilter type;

    private StringFilter url;

    private LongFilter exerciseId;

    public ExerciseResourceCriteria() {}

    public ExerciseResourceCriteria(ExerciseResourceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.exerciseId = other.exerciseId == null ? null : other.exerciseId.copy();
    }

    @Override
    public ExerciseResourceCriteria copy() {
        return new ExerciseResourceCriteria(this);
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

    public ExerciseResourceTypeFilter getType() {
        return type;
    }

    public ExerciseResourceTypeFilter type() {
        if (type == null) {
            type = new ExerciseResourceTypeFilter();
        }
        return type;
    }

    public void setType(ExerciseResourceTypeFilter type) {
        this.type = type;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
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
        final ExerciseResourceCriteria that = (ExerciseResourceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(order, that.order) &&
            Objects.equals(type, that.type) &&
            Objects.equals(url, that.url) &&
            Objects.equals(exerciseId, that.exerciseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, order, type, url, exerciseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseResourceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (exerciseId != null ? "exerciseId=" + exerciseId + ", " : "") +
            "}";
    }
}
