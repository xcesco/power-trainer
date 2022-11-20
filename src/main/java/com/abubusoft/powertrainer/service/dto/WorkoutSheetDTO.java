package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.WorkoutSheet} entity.
 */
public class WorkoutSheetDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private String name;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private String description;

    private String owner;

    private Integer prepareTime;

    private Integer coolDownTime;

    private Integer cycles;

    private Integer cycleRestTime;

    private Integer set;

    private Integer setRestTime;

    private WorkoutType type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public Integer getCoolDownTime() {
        return coolDownTime;
    }

    public void setCoolDownTime(Integer coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public Integer getCycles() {
        return cycles;
    }

    public void setCycles(Integer cycles) {
        this.cycles = cycles;
    }

    public Integer getCycleRestTime() {
        return cycleRestTime;
    }

    public void setCycleRestTime(Integer cycleRestTime) {
        this.cycleRestTime = cycleRestTime;
    }

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public Integer getSetRestTime() {
        return setRestTime;
    }

    public void setSetRestTime(Integer setRestTime) {
        this.setRestTime = setRestTime;
    }

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutSheetDTO)) {
            return false;
        }

        WorkoutSheetDTO workoutSheetDTO = (WorkoutSheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutSheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutSheetDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
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
