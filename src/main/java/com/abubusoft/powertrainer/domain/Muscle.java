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
 * A Muscle.
 */
@Entity
@Table(name = "muscles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Muscle implements Serializable {

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

    @Column(name = "note")
    private String note;

    @ManyToMany(mappedBy = "muscles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exerciseResources", "muscles", "exerciseTools" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Muscle id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Muscle uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Muscle name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Muscle image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Muscle imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getNote() {
        return this.note;
    }

    public Muscle note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public Muscle exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public Muscle addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.getMuscles().add(this);
        return this;
    }

    public Muscle removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.getMuscles().remove(this);
        return this;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.removeMuscle(this));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.addMuscle(this));
        }
        this.exercises = exercises;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Muscle)) {
            return false;
        }
        return id != null && id.equals(((Muscle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Muscle{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
