package com.abubusoft.powertrainer.backend.repositories.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity {
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  protected long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseEntity baseEntity = (BaseEntity) o;
    return id == baseEntity.id &&
            UUID.equals(baseEntity.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, UUID);
  }

  protected String UUID;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUUID() {
    return UUID;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }
}
