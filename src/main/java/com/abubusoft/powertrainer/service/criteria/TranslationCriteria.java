package com.abubusoft.powertrainer.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Translation} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.TranslationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /translations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TranslationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter entityType;

    private StringFilter entityUuid;

    private StringFilter value;

    private StringFilter entityField;

    private LongFilter languageId;

    public TranslationCriteria() {}

    public TranslationCriteria(TranslationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entityType = other.entityType == null ? null : other.entityType.copy();
        this.entityUuid = other.entityUuid == null ? null : other.entityUuid.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.entityField = other.entityField == null ? null : other.entityField.copy();
        this.languageId = other.languageId == null ? null : other.languageId.copy();
    }

    @Override
    public TranslationCriteria copy() {
        return new TranslationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEntityType() {
        return entityType;
    }

    public StringFilter entityType() {
        if (entityType == null) {
            entityType = new StringFilter();
        }
        return entityType;
    }

    public void setEntityType(StringFilter entityType) {
        this.entityType = entityType;
    }

    public StringFilter getEntityUuid() {
        return entityUuid;
    }

    public StringFilter entityUuid() {
        if (entityUuid == null) {
            entityUuid = new StringFilter();
        }
        return entityUuid;
    }

    public void setEntityUuid(StringFilter entityUuid) {
        this.entityUuid = entityUuid;
    }

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getEntityField() {
        return entityField;
    }

    public StringFilter entityField() {
        if (entityField == null) {
            entityField = new StringFilter();
        }
        return entityField;
    }

    public void setEntityField(StringFilter entityField) {
        this.entityField = entityField;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public LongFilter languageId() {
        if (languageId == null) {
            languageId = new LongFilter();
        }
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TranslationCriteria that = (TranslationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(entityUuid, that.entityUuid) &&
            Objects.equals(value, that.value) &&
            Objects.equals(entityField, that.entityField) &&
            Objects.equals(languageId, that.languageId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType, entityUuid, value, entityField, languageId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TranslationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entityType != null ? "entityType=" + entityType + ", " : "") +
            (entityUuid != null ? "entityUuid=" + entityUuid + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (entityField != null ? "entityField=" + entityField + ", " : "") +
            (languageId != null ? "languageId=" + languageId + ", " : "") +
            "}";
    }
}
