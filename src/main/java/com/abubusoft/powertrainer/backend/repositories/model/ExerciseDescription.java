package com.abubusoft.powertrainer.backend.repositories.model;

import java.io.Serializable;

public class ExerciseDescription implements Serializable {
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
