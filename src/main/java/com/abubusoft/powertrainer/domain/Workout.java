package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Workout.
 */
@Entity
@Table(name = "workouts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Workout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkoutType type;

    @Column(name = "execution_time")
    private Integer executionTime;

    @Column(name = "preview_time")
    private Integer previewTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkoutStatus status;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "workout")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workout" }, allowSetters = true)
    private Set<WorkoutStep> workoutSteps = new HashSet<>();

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

    public Workout id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Workout uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Workout name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Workout image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Workout imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public WorkoutType getType() {
        return this.type;
    }

    public Workout type(WorkoutType type) {
        this.type = type;
        return this;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    public Integer getExecutionTime() {
        return this.executionTime;
    }

    public Workout executionTime(Integer executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getPreviewTime() {
        return this.previewTime;
    }

    public Workout previewTime(Integer previewTime) {
        this.previewTime = previewTime;
        return this;
    }

    public void setPreviewTime(Integer previewTime) {
        this.previewTime = previewTime;
    }

    public WorkoutStatus getStatus() {
        return this.status;
    }

    public Workout status(WorkoutStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Workout date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getNote() {
        return this.note;
    }

    public Workout note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<WorkoutStep> getWorkoutSteps() {
        return this.workoutSteps;
    }

    public Workout workoutSteps(Set<WorkoutStep> workoutSteps) {
        this.setWorkoutSteps(workoutSteps);
        return this;
    }

    public Workout addWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.add(workoutStep);
        workoutStep.setWorkout(this);
        return this;
    }

    public Workout removeWorkoutStep(WorkoutStep workoutStep) {
        this.workoutSteps.remove(workoutStep);
        workoutStep.setWorkout(null);
        return this;
    }

    public void setWorkoutSteps(Set<WorkoutStep> workoutSteps) {
        if (this.workoutSteps != null) {
            this.workoutSteps.forEach(i -> i.setWorkout(null));
        }
        if (workoutSteps != null) {
            workoutSteps.forEach(i -> i.setWorkout(this));
        }
        this.workoutSteps = workoutSteps;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public Workout calendar(Calendar calendar) {
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
        if (!(o instanceof Workout)) {
            return false;
        }
        return id != null && id.equals(((Workout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Workout{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", type='" + getType() + "'" +
            ", executionTime=" + getExecutionTime() +
            ", previewTime=" + getPreviewTime() +
            ", status='" + getStatus() + "'" +
            ", date='" + getDate() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
