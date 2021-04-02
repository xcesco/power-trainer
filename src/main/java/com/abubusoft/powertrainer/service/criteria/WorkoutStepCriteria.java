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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private IntegerFilter order;

    private IntegerFilter value;

    private ValueTypeFilter valueType;

    private IntegerFilter executionTime;

    private WorkoutStepTypeFilter type;

    private WorkoutStatusFilter status;

    public WorkoutStepCriteria() {}

    public WorkoutStepCriteria(WorkoutStepCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.valueType = other.valueType == null ? null : other.valueType.copy();
        this.executionTime = other.executionTime == null ? null : other.executionTime.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.status = other.status == null ? null : other.status.copy();
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
            Objects.equals(value, that.value) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(executionTime, that.executionTime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, order, value, valueType, executionTime, type, status);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStepCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (valueType != null ? "valueType=" + valueType + ", " : "") +
            (executionTime != null ? "executionTime=" + executionTime + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            "}";
    }
}
