package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.NoteType;
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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Note} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NoteType
     */
    public static class NoteTypeFilter extends Filter<NoteType> {

        public NoteTypeFilter() {}

        public NoteTypeFilter(NoteTypeFilter filter) {
            super(filter);
        }

        @Override
        public NoteTypeFilter copy() {
            return new NoteTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private NoteTypeFilter type;

    private StringFilter url;

    private LongFilter exerciseId;

    public NoteCriteria() {}

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.exerciseId = other.exerciseId == null ? null : other.exerciseId.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
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

    public NoteTypeFilter getType() {
        return type;
    }

    public NoteTypeFilter type() {
        if (type == null) {
            type = new NoteTypeFilter();
        }
        return type;
    }

    public void setType(NoteTypeFilter type) {
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
        final NoteCriteria that = (NoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(type, that.type) &&
            Objects.equals(url, that.url) &&
            Objects.equals(exerciseId, that.exerciseId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, type, url, exerciseId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (exerciseId != null ? "exerciseId=" + exerciseId + ", " : "") +
            "}";
    }
}
