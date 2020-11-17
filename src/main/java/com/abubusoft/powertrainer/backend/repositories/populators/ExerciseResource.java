package com.abubusoft.powertrainer.backend.repositories.populators;

import java.util.Objects;
import java.util.StringJoiner;

public class ExerciseResource {
  private String UUID;
  private String name;
  private String resource;

  @Override
  public String toString() {
    return new StringJoiner(", ", ExerciseResource.class.getSimpleName() + "[", "]")
            .add("UUID='" + UUID + "'")
            .add("name='" + name + "'")
            .add("resource='" + resource + "'")
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExerciseResource that = (ExerciseResource) o;
    return UUID.equals(that.UUID) &&
            name.equals(that.name) &&
            resource.equals(that.resource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID, name, resource);
  }

  public String getUUID() {
    return UUID;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }
}
