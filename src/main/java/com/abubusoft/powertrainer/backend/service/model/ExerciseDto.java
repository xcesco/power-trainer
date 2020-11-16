package com.abubusoft.powertrainer.backend.service.model;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

public class ExerciseDto extends RepresentationModel<ExerciseDto> implements Serializable {
  public ExerciseDto(String UUID, String name, String image, List<String> videoUrls, String lastUpdate, List<String> muscles, String description, List<String> equipments) {
    this.UUID = UUID;
    this.name = name;
    this.image = image;
    this.videoUrls = videoUrls;
    this.lastUpdate = lastUpdate;
    this.muscles = muscles;
    this.description = description;
    this.equipments = equipments;
  }

  public String getUUID() {
    return UUID;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public List<String> getVideoUrls() {
    return videoUrls;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public List<String> getMuscles() {
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
  private final String image;
  private final List<String> videoUrls;
  private final String lastUpdate;
  private final List<String> muscles;
  private final String description;
  private final List<String> equipments;
}
