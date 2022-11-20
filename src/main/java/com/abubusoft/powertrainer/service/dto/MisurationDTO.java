package com.abubusoft.powertrainer.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.Misuration} entity.
 */
public class MisurationDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID uuid;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private Integer value;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private String note;

    private CalendarDTO calendar;

    private MisurationTypeDTO misurationType;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    public MisurationTypeDTO getMisurationType() {
        return misurationType;
    }

    public void setMisurationType(MisurationTypeDTO misurationType) {
        this.misurationType = misurationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MisurationDTO)) {
            return false;
        }

        MisurationDTO misurationDTO = (MisurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, misurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MisurationDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", date='" + getDate() + "'" +
            ", value=" + getValue() +
            ", image='" + getImage() + "'" +
            ", note='" + getNote() + "'" +
            ", calendar=" + getCalendar() +
            ", misurationType=" + getMisurationType() +
            "}";
    }
}
