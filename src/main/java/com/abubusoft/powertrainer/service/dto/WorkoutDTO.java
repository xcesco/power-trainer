package com.abubusoft.powertrainer.service.dto;

import com.abubusoft.powertrainer.domain.enumeration.WorkoutStatus;
import com.abubusoft.powertrainer.domain.enumeration.WorkoutType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.Workout} entity.
 */
public class WorkoutDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    private String name;

    @Lob
    private byte[] image;

    private String imageContentType;
    private WorkoutType type;

    private Integer executionTime;

    private Integer previewTime;

    private WorkoutStatus status;

    private ZonedDateTime date;

    private String note;

    private CalendarDTO calendar;

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

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(Integer previewTime) {
        this.previewTime = previewTime;
    }

    public WorkoutStatus getStatus() {
        return status;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CalendarDTO getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarDTO calendar) {
        this.calendar = calendar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkoutDTO)) {
            return false;
        }

        WorkoutDTO workoutDTO = (WorkoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkoutDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", type='" + getType() + "'" +
            ", executionTime=" + getExecutionTime() +
            ", previewTime=" + getPreviewTime() +
            ", status='" + getStatus() + "'" +
            ", date='" + getDate() + "'" +
            ", note='" + getNote() + "'" +
            ", calendar=" + getCalendar() +
            "}";
    }
}
