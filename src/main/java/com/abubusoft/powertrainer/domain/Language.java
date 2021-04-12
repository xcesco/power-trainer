package com.abubusoft.powertrainer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Language.
 */
@Entity
@Table(name = "languages")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "language")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "language" }, allowSetters = true)
    private Set<Translation> translations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Language code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Language name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Translation> getTranslations() {
        return this.translations;
    }

    public Language translations(Set<Translation> translations) {
        this.setTranslations(translations);
        return this;
    }

    public Language addTranslation(Translation translation) {
        this.translations.add(translation);
        translation.setLanguage(this);
        return this;
    }

    public Language removeTranslation(Translation translation) {
        this.translations.remove(translation);
        translation.setLanguage(null);
        return this;
    }

    public void setTranslations(Set<Translation> translations) {
        if (this.translations != null) {
            this.translations.forEach(i -> i.setLanguage(null));
        }
        if (translations != null) {
            translations.forEach(i -> i.setLanguage(this));
        }
        this.translations = translations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return id != null && id.equals(((Language) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
