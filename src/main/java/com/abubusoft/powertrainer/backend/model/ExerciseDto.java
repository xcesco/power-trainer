package com.abubusoft.powertrainer.backend.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ExerciseDto implements Serializable {
  public ExerciseDto(String UUID, String name, List<String> videoUrls, String lastUpdate, List<MuscleType> muscles, String description, List<String> equipments) {
    this.UUID = UUID;
    this.name = name;
    this.videoUrls = videoUrls != null ? videoUrls : Collections.emptyList();
    this.lastUpdate = lastUpdate;
    this.muscles = muscles != null ? muscles : Collections.emptyList();
    this.description = description;
    this.equipments = equipments != null ? equipments : Collections.emptyList();
  }

  public String getUUID() {
    return UUID;
  }

  public String getName() {
    return name;
  }

  public List<String> getVideoUrls() {
    return videoUrls;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public List<MuscleType> getMuscles() {
    return muscles;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getEquipments() {
    return equipments;
  }

  private final String UUID;
  private final String name;
  private final List<String> videoUrls;
  private final String lastUpdate;
  private final List<MuscleType> muscles;
  private final String description;
  private final List<String> equipments;
}
