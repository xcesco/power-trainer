package com.abubusoft.powertrainer.domain;

import com.abubusoft.powertrainer.domain.enumeration.ExerciseResourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ExerciseResource.
 */
@Entity
@Table(name = "exercise_resources")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExerciseResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "jhi_order")
    private Integer order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExerciseResourceType type;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exerciseResources", "muscles", "exerciseTools" }, allowSetters = true)
    private Exercise exercise;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExerciseResource id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ExerciseResource uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getOrder() {
        return this.order;
    }

    public ExerciseResource order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ExerciseResourceType getType() {
        return this.type;
    }

    public ExerciseResource type(ExerciseResourceType type) {
        this.type = type;
        return this;
    }

    public void setType(ExerciseResourceType type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public ExerciseResource url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ExerciseResource image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ExerciseResource imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return this.description;
    }

    public ExerciseResource description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public ExerciseResource exercise(Exercise exercise) {
        this.setExercise(exercise);
        return this;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExerciseResource)) {
            return false;
        }
        return id != null && id.equals(((ExerciseResource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseResource{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", order=" + getOrder() +
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
