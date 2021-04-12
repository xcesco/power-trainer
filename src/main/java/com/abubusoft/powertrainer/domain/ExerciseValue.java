package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExerciseValue.
 */
@Entity
@Table(name = "exercise_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExerciseValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "exercise_uuid", nullable = false)
    private UUID exerciseUuid;

    @NotNull
    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @NotNull
    @Column(name = "exercise_value", nullable = false)
    private Integer exerciseValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_value_type", nullable = false)
    private ValueType exerciseValueType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exerciseValues", "misurations", "workouts" }, allowSetters = true)
    private Calendar calendar;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExerciseValue id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ExerciseValue uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public ExerciseValue date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UUID getExerciseUuid() {
        return this.exerciseUuid;
    }

    public ExerciseValue exerciseUuid(UUID exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
        return this;
    }

    public void setExerciseUuid(UUID exerciseUuid) {
        this.exerciseUuid = exerciseUuid;
    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public ExerciseValue exerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
        return this;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getExerciseValue() {
        return this.exerciseValue;
    }

    public ExerciseValue exerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
        return this;
    }

    public void setExerciseValue(Integer exerciseValue) {
        this.exerciseValue = exerciseValue;
    }

    public ValueType getExerciseValueType() {
        return this.exerciseValueType;
    }

    public ExerciseValue exerciseValueType(ValueType exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
        return this;
    }

    public void setExerciseValueType(ValueType exerciseValueType) {
        this.exerciseValueType = exerciseValueType;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public ExerciseValue calendar(Calendar calendar) {
        this.setCalendar(calendar);
        return this;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseValue)) {
            return false;
        }
        return id != null && id.equals(((ExerciseValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseValue{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", date='" + getDate() + "'" +
            ", exerciseUuid='" + getExerciseUuid() + "'" +
            ", exerciseName='" + getExerciseName() + "'" +
            ", exerciseValue=" + getExerciseValue() +
            ", exerciseValueType='" + getExerciseValueType() + "'" +
            "}";
    }
}
