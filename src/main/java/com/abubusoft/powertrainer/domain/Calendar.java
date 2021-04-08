package com.abubusoft.powertrainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Calendar.
 */
@Entity
@Table(name = "calendars")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @OneToMany(mappedBy = "calendar")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "calendar" }, allowSetters = true)
    private Set<ExerciseValue> exerciseValues = new HashSet<>();

    @OneToMany(mappedBy = "calendar")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "calendar", "misurationType" }, allowSetters = true)
    private Set<Misuration> misurations = new HashSet<>();

    @OneToMany(mappedBy = "calendar")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutSteps", "calendar" }, allowSetters = true)
    private Set<Workout> workouts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Calendar uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Calendar name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return this.owner;
    }

    public Calendar owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<ExerciseValue> getExerciseValues() {
        return this.exerciseValues;
    }

    public Calendar exerciseValues(Set<ExerciseValue> exerciseValues) {
        this.setExerciseValues(exerciseValues);
        return this;
    }

    public Calendar addExerciseValue(ExerciseValue exerciseValue) {
        this.exerciseValues.add(exerciseValue);
        exerciseValue.setCalendar(this);
        return this;
    }

    public Calendar removeExerciseValue(ExerciseValue exerciseValue) {
        this.exerciseValues.remove(exerciseValue);
        exerciseValue.setCalendar(null);
        return this;
    }

    public void setExerciseValues(Set<ExerciseValue> exerciseValues) {
        if (this.exerciseValues != null) {
            this.exerciseValues.forEach(i -> i.setCalendar(null));
        }
        if (exerciseValues != null) {
            exerciseValues.forEach(i -> i.setCalendar(this));
        }
        this.exerciseValues = exerciseValues;
    }

    public Set<Misuration> getMisurations() {
        return this.misurations;
    }

    public Calendar misurations(Set<Misuration> misurations) {
        this.setMisurations(misurations);
        return this;
    }

    public Calendar addMisuration(Misuration misuration) {
        this.misurations.add(misuration);
        misuration.setCalendar(this);
        return this;
    }

    public Calendar removeMisuration(Misuration misuration) {
        this.misurations.remove(misuration);
        misuration.setCalendar(null);
        return this;
    }

    public void setMisurations(Set<Misuration> misurations) {
        if (this.misurations != null) {
            this.misurations.forEach(i -> i.setCalendar(null));
        }
        if (misurations != null) {
            misurations.forEach(i -> i.setCalendar(this));
        }
        this.misurations = misurations;
    }

    public Set<Workout> getWorkouts() {
        return this.workouts;
    }

    public Calendar workouts(Set<Workout> workouts) {
        this.setWorkouts(workouts);
        return this;
    }

    public Calendar addWorkout(Workout workout) {
        this.workouts.add(workout);
        workout.setCalendar(this);
        return this;
    }

    public Calendar removeWorkout(Workout workout) {
        this.workouts.remove(workout);
        workout.setCalendar(null);
        return this;
    }

    public void setWorkouts(Set<Workout> workouts) {
        if (this.workouts != null) {
            this.workouts.forEach(i -> i.setCalendar(null));
        }
        if (workouts != null) {
            workouts.forEach(i -> i.setCalendar(this));
        }
        this.workouts = workouts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calendar)) {
            return false;
        }
        return id != null && id.equals(((Calendar) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calendar{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
