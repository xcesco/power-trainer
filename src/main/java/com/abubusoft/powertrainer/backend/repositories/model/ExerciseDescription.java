package com.abubusoft.powertrainer.backend.repositories.model;

public class ExerciseDescription {
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  private String language;
  private String value;
}
