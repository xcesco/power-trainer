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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Misuration} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.MisurationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /misurations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MisurationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private ZonedDateTimeFilter date;

    private IntegerFilter value;

    private LongFilter calendarId;

    private LongFilter misurationTypeId;

    public MisurationCriteria() {}

    public MisurationCriteria(MisurationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.calendarId = other.calendarId == null ? null : other.calendarId.copy();
        this.misurationTypeId = other.misurationTypeId == null ? null : other.misurationTypeId.copy();
    }

    @Override
    public MisurationCriteria copy() {
        return new MisurationCriteria(this);
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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            date = new ZonedDateTimeFilter();
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public IntegerFilter getValue() {
        return value;
    }

    public IntegerFilter value() {
        if (value == null) {
            value = new IntegerFilter();
        }
        return value;
    }

    public void setValue(IntegerFilter value) {
        this.value = value;
    }

    public LongFilter getCalendarId() {
        return calendarId;
    }

    public LongFilter calendarId() {
        if (calendarId == null) {
            calendarId = new LongFilter();
        }
        return calendarId;
    }

    public void setCalendarId(LongFilter calendarId) {
        this.calendarId = calendarId;
    }

    public LongFilter getMisurationTypeId() {
        return misurationTypeId;
    }

    public LongFilter misurationTypeId() {
        if (misurationTypeId == null) {
            misurationTypeId = new LongFilter();
        }
        return misurationTypeId;
    }

    public void setMisurationTypeId(LongFilter misurationTypeId) {
        this.misurationTypeId = misurationTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MisurationCriteria that = (MisurationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(date, that.date) &&
            Objects.equals(value, that.value) &&
            Objects.equals(calendarId, that.calendarId) &&
            Objects.equals(misurationTypeId, that.misurationTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, date, value, calendarId, misurationTypeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MisurationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (calendarId != null ? "calendarId=" + calendarId + ", " : "") +
            (misurationTypeId != null ? "misurationTypeId=" + misurationTypeId + ", " : "") +
            "}";
    }
}
