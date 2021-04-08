package com.abubusoft.powertrainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Translation.
 */
@Entity
@Table(name = "translations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Translation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @NotNull
    @Column(name = "entity_uuid", nullable = false)
    private String entityUuid;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "translations" }, allowSetters = true)
    private Language language;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Translation id(Long id) {
        this.id = id;
        return this;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public Translation entityType(String entityType) {
        this.entityType = entityType;
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityUuid() {
        return this.entityUuid;
    }

    public Translation entityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
        return this;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    public String getValue() {
        return this.value;
    }

    public Translation value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Translation language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Translation)) {
            return false;
        }
        return id != null && id.equals(((Translation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Translation{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityUuid='" + getEntityUuid() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
