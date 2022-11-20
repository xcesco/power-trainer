package com.abubusoft.powertrainer.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.ExerciseTool} entity.
 */
public class ExerciseToolDTO implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseToolDTO)) {
            return false;
        }

        ExerciseToolDTO exerciseToolDTO = (ExerciseToolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exerciseToolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseToolDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", image='" + getImage() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
