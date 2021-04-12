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
 * Criteria class for the {@link com.abubusoft.powertrainer.domain.Language} entity. This class is used
 * in {@link com.abubusoft.powertrainer.web.rest.LanguageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /languages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LanguageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private LongFilter translationId;

    public LanguageCriteria() {}

    public LanguageCriteria(LanguageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.translationId = other.translationId == null ? null : other.translationId.copy();
    }

    @Override
    public LanguageCriteria copy() {
        return new LanguageCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getTranslationId() {
        return translationId;
    }

    public LongFilter translationId() {
        if (translationId == null) {
            translationId = new LongFilter();
        }
        return translationId;
    }

    public void setTranslationId(LongFilter translationId) {
        this.translationId = translationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LanguageCriteria that = (LanguageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(translationId, that.translationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, translationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (translationId != null ? "translationId=" + translationId + ", " : "") +
            "}";
    }
}
