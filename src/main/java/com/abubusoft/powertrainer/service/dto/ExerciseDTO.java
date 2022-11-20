package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.ValueType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.Exercise} entity.
 */
public class ExerciseDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    @Lob
    private byte[] image;

    private String imageContentType;

    @NotNull
    private String name;

    @Lob
    private String description;

    private ValueType valueType;

    @NotNull
    private String owner;

    private Set<MuscleDTO> muscles = new HashSet<>();

    private Set<ExerciseToolDTO> exerciseTools = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<MuscleDTO> getMuscles() {
        return muscles;
    }

    public void setMuscles(Set<MuscleDTO> muscles) {
        this.muscles = muscles;
    }

    public Set<ExerciseToolDTO> getExerciseTools() {
        return exerciseTools;
    }

    public void setExerciseTools(Set<ExerciseToolDTO> exerciseTools) {
        this.exerciseTools = exerciseTools;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseDTO)) {
            return false;
        }

        ExerciseDTO exerciseDTO = (ExerciseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exerciseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", image='" + getImage() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", owner='" + getOwner() + "'" +
            ", muscles=" + getMuscles() +
            ", exerciseTools=" + getExerciseTools() +
            "}";
    }
}
