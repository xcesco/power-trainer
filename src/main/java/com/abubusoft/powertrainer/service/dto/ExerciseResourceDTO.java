package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.ExerciseResourceType;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.ExerciseResource} entity.
 */
public class ExerciseResourceDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    private Integer order;

    @NotNull
    private ExerciseResourceType type;

    private String url;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private String description;

    private ExerciseDTO exercise;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ExerciseResourceType getType() {
        return type;
    }

    public void setType(ExerciseResourceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseResourceDTO)) {
            return false;
        }

        ExerciseResourceDTO exerciseResourceDTO = (ExerciseResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exerciseResourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseResourceDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", exercise=" + getExercise() +
            "}";
    }
}
