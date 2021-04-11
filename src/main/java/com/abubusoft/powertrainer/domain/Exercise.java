package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
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
 * A Exercise.
 */
@Entity
@Table(name = "exercises")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exercise implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private ValueType valueType;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @OneToMany(mappedBy = "exercise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exercise" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_exercises__muscle",
        joinColumns = @JoinColumn(name = "exercises_id"),
        inverseJoinColumns = @JoinColumn(name = "muscle_id")
    )
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private Set<Muscle> muscles = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_exercises__exercise_tool",
        joinColumns = @JoinColumn(name = "exercises_id"),
        inverseJoinColumns = @JoinColumn(name = "exercise_tool_id")
    )
    @JsonIgnoreProperties(value = { "exercises" }, allowSetters = true)
    private Set<ExerciseTool> exerciseTools = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exercise id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Exercise uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Exercise image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Exercise imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getName() {
        return this.name;
    }

    public Exercise name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Exercise description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public Exercise valueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getOwner() {
        return this.owner;
    }

    public Exercise owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public Exercise notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Exercise addNote(Note note) {
        this.notes.add(note);
        note.setExercise(this);
        return this;
    }

    public Exercise removeNote(Note note) {
        this.notes.remove(note);
        note.setExercise(null);
        return this;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setExercise(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setExercise(this));
        }
        this.notes = notes;
    }

    public Set<Muscle> getMuscles() {
        return this.muscles;
    }

    public Exercise muscles(Set<Muscle> muscles) {
        this.setMuscles(muscles);
        return this;
    }

    public Exercise addMuscle(Muscle muscle) {
        this.muscles.add(muscle);
        muscle.getExercises().add(this);
        return this;
    }

    public Exercise removeMuscle(Muscle muscle) {
        this.muscles.remove(muscle);
        muscle.getExercises().remove(this);
        return this;
    }

    public void setMuscles(Set<Muscle> muscles) {
        this.muscles = muscles;
    }

    public Set<ExerciseTool> getExerciseTools() {
        return this.exerciseTools;
    }

    public Exercise exerciseTools(Set<ExerciseTool> exerciseTools) {
        this.setExerciseTools(exerciseTools);
        return this;
    }

    public Exercise addExerciseTool(ExerciseTool exerciseTool) {
        this.exerciseTools.add(exerciseTool);
        exerciseTool.getExercises().add(this);
        return this;
    }

    public Exercise removeExerciseTool(ExerciseTool exerciseTool) {
        this.exerciseTools.remove(exerciseTool);
        exerciseTool.getExercises().remove(this);
        return this;
    }

    public void setExerciseTools(Set<ExerciseTool> exerciseTools) {
        this.exerciseTools = exerciseTools;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exercise)) {
            return false;
        }
        return id != null && id.equals(((Exercise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exercise{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
