package com.abubusoft.powertrainer.service.criteria;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Workout} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.WorkoutResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /workouts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkoutCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private WorkoutTypeFilter type;

    private IntegerFilter executionTime;

    private IntegerFilter previewTime;

    private WorkoutStatusFilter status;

    private ZonedDateTimeFilter date;

    private StringFilter note;

    private LongFilter workoutStepId;

    private LongFilter calendarId;

    public WorkoutCriteria() {}

    public WorkoutCriteria(WorkoutCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.executionTime = other.executionTime == null ? null : other.executionTime.copy();
        this.previewTime = other.previewTime == null ? null : other.previewTime.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.workoutStepId = other.workoutStepId == null ? null : other.workoutStepId.copy();
        this.calendarId = other.calendarId == null ? null : other.calendarId.copy();
    }

    @Override
    public WorkoutCriteria copy() {
        return new WorkoutCriteria(this);
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

    public IntegerFilter getPreviewTime() {
        return previewTime;
    }

    public IntegerFilter previewTime() {
        if (previewTime == null) {
            previewTime = new IntegerFilter();
        }
        return previewTime;
    }

    public void setPreviewTime(IntegerFilter previewTime) {
        this.previewTime = previewTime;
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

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getWorkoutStepId() {
        return workoutStepId;
    }

    public LongFilter workoutStepId() {
        if (workoutStepId == null) {
            workoutStepId = new LongFilter();
        }
        return workoutStepId;
    }

    public void setWorkoutStepId(LongFilter workoutStepId) {
        this.workoutStepId = workoutStepId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkoutCriteria that = (WorkoutCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(executionTime, that.executionTime) &&
            Objects.equals(previewTime, that.previewTime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(date, that.date) &&
            Objects.equals(note, that.note) &&
            Objects.equals(workoutStepId, that.workoutStepId) &&
            Objects.equals(calendarId, that.calendarId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, type, executionTime, previewTime, status, date, note, workoutStepId, calendarId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (executionTime != null ? "executionTime=" + executionTime + ", " : "") +
            (previewTime != null ? "previewTime=" + previewTime + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (workoutStepId != null ? "workoutStepId=" + workoutStepId + ", " : "") +
            (calendarId != null ? "calendarId=" + calendarId + ", " : "") +
            "}";
    }
}
