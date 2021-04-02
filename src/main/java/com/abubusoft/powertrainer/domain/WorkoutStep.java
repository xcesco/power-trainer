package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutStepType;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkoutStep.
 */
@Entity
@Table(name = "workout_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkoutStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "value")
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private ValueType valueType;

    @Column(name = "execution_time")
    private Integer executionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkoutStepType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkoutStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutStep id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public WorkoutStep uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getOrder() {
        return this.order;
    }

    public WorkoutStep order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getValue() {
        return this.value;
    }

    public WorkoutStep value(Integer value) {
        this.value = value;
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public WorkoutStep valueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public Integer getExecutionTime() {
        return this.executionTime;
    }

    public WorkoutStep executionTime(Integer executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public WorkoutStepType getType() {
        return this.type;
    }

    public WorkoutStep type(WorkoutStepType type) {
        this.type = type;
        return this;
    }

    public void setType(WorkoutStepType type) {
        this.type = type;
    }

    public WorkoutStatus getStatus() {
        return this.status;
    }

    public WorkoutStep status(WorkoutStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutStep)) {
            return false;
        }
        return id != null && id.equals(((WorkoutStep) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutStep{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", value=" + getValue() +
            ", valueType='" + getValueType() + "'" +
            ", executionTime=" + getExecutionTime() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
