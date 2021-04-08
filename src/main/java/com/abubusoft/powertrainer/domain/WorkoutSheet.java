package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A WorkoutSheet.
 */
@Entity
@Table(name = "workout_sheets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkoutSheet implements Serializable {

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

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Column(name = "owner")
    private String owner;

    @Column(name = "prepare_time")
    private Integer prepareTime;

    @Column(name = "cool_down_time")
    private Integer coolDownTime;

    @Column(name = "cycles")
    private Integer cycles;

    @Column(name = "cycle_rest_time")
    private Integer cycleRestTime;

    @Column(name = "set")
    private Integer set;

    @Column(name = "set_rest_time")
    private Integer setRestTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkoutType type;

    @OneToMany(mappedBy = "workoutSheet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workoutSheet" }, allowSetters = true)
    private Set<WorkoutSheetExercise> workoutSheetExercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutSheet id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public WorkoutSheet uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public WorkoutSheet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return this.image;
    }

    public WorkoutSheet image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public WorkoutSheet imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return this.description;
    }

    public WorkoutSheet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return this.owner;
    }

    public WorkoutSheet owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPrepareTime() {
        return this.prepareTime;
    }

    public WorkoutSheet prepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
        return this;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public Integer getCoolDownTime() {
        return this.coolDownTime;
    }

    public WorkoutSheet coolDownTime(Integer coolDownTime) {
        this.coolDownTime = coolDownTime;
        return this;
    }

    public void setCoolDownTime(Integer coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public Integer getCycles() {
        return this.cycles;
    }

    public WorkoutSheet cycles(Integer cycles) {
        this.cycles = cycles;
        return this;
    }

    public void setCycles(Integer cycles) {
        this.cycles = cycles;
    }

    public Integer getCycleRestTime() {
        return this.cycleRestTime;
    }

    public WorkoutSheet cycleRestTime(Integer cycleRestTime) {
        this.cycleRestTime = cycleRestTime;
        return this;
    }

    public void setCycleRestTime(Integer cycleRestTime) {
        this.cycleRestTime = cycleRestTime;
    }

    public Integer getSet() {
        return this.set;
    }

    public WorkoutSheet set(Integer set) {
        this.set = set;
        return this;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Integer getSetRestTime() {
        return this.setRestTime;
    }

    public WorkoutSheet setRestTime(Integer setRestTime) {
        this.setRestTime = setRestTime;
        return this;
    }

    public void setSetRestTime(Integer setRestTime) {
        this.setRestTime = setRestTime;
    }

    public WorkoutType getType() {
        return this.type;
    }

    public WorkoutSheet type(WorkoutType type) {
        this.type = type;
        return this;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    public Set<WorkoutSheetExercise> getWorkoutSheetExercises() {
        return this.workoutSheetExercises;
    }

    public WorkoutSheet workoutSheetExercises(Set<WorkoutSheetExercise> workoutSheetExercises) {
        this.setWorkoutSheetExercises(workoutSheetExercises);
        return this;
    }

    public WorkoutSheet addWorkoutSheetExercise(WorkoutSheetExercise workoutSheetExercise) {
        this.workoutSheetExercises.add(workoutSheetExercise);
        workoutSheetExercise.setWorkoutSheet(this);
        return this;
    }

    public WorkoutSheet removeWorkoutSheetExercise(WorkoutSheetExercise workoutSheetExercise) {
        this.workoutSheetExercises.remove(workoutSheetExercise);
        workoutSheetExercise.setWorkoutSheet(null);
        return this;
    }

    public void setWorkoutSheetExercises(Set<WorkoutSheetExercise> workoutSheetExercises) {
        if (this.workoutSheetExercises != null) {
            this.workoutSheetExercises.forEach(i -> i.setWorkoutSheet(null));
        }
        if (workoutSheetExercises != null) {
            workoutSheetExercises.forEach(i -> i.setWorkoutSheet(this));
        }
        this.workoutSheetExercises = workoutSheetExercises;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutSheet)) {
            return false;
        }
        return id != null && id.equals(((WorkoutSheet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheet{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", description='" + getDescription() + "'" +
            ", owner='" + getOwner() + "'" +
            ", prepareTime=" + getPrepareTime() +
            ", coolDownTime=" + getCoolDownTime() +
            ", cycles=" + getCycles() +
            ", cycleRestTime=" + getCycleRestTime() +
            ", set=" + getSet() +
            ", setRestTime=" + getSetRestTime() +
            ", type='" + getType() + "'" +
            "}";
    }
}
