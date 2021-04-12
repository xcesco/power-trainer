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
import org.hibernate.annotations.Type;

/**
 * A ExerciseTool.
 */
@Entity
@Table(name = "exercise_tools")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExerciseTool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "exerciseTools")
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

    public ExerciseTool id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ExerciseTool uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ExerciseTool image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ExerciseTool imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getName() {
        return this.name;
    }

    public ExerciseTool name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExerciseTool description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public ExerciseTool exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public ExerciseTool addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.getExerciseTools().add(this);
        return this;
    }

    public ExerciseTool removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.getExerciseTools().remove(this);
        return this;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.removeExerciseTool(this));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.addExerciseTool(this));
        }
        this.exercises = exercises;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseTool)) {
            return false;
        }
        return id != null && id.equals(((ExerciseTool) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseTool{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
