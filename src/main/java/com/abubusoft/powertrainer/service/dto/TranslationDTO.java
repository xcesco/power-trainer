package com.abubusoft.powertrainer.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abubusoft.powertrainer.domain.Translation} entity.
 */
public class TranslationDTO implements Serializable {

    private Long id;

    @NotNull
    private String entityType;

    @NotNull
    private String entityUuid;

    @NotNull
    private String value;

    private LanguageDTO language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TranslationDTO)) {
            return false;
        }

        TranslationDTO translationDTO = (TranslationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, translationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TranslationDTO{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityUuid='" + getEntityUuid() + "'" +
            ", value='" + getValue() + "'" +
            ", language=" + getLanguage() +
            "}";
    }
}
