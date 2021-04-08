package com.abubusoft.powertrainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Misuration.
 */
@Entity
@Table(name = "misurations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Misuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exerciseValues", "misurations", "workouts" }, allowSetters = true)
    private Calendar calendar;

    @ManyToOne
    @JsonIgnoreProperties(value = { "misurations" }, allowSetters = true)
    private MisurationType misurationType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Misuration id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Misuration uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Misuration date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getValue() {
        return this.value;
    }

    public Misuration value(Integer value) {
        this.value = value;
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Misuration image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Misuration imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getNote() {
        return this.note;
    }

    public Misuration note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public Misuration calendar(Calendar calendar) {
        this.setCalendar(calendar);
        return this;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public MisurationType getMisurationType() {
        return this.misurationType;
    }

    public Misuration misurationType(MisurationType misurationType) {
        this.setMisurationType(misurationType);
        return this;
    }

    public void setMisurationType(MisurationType misurationType) {
        this.misurationType = misurationType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Misuration)) {
            return false;
        }
        return id != null && id.equals(((Misuration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Misuration{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", date='" + getDate() + "'" +
            ", value=" + getValue() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
