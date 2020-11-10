package com.abubusoft.powertrainer.backend.repositories.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Exercise {
  private String UUID;
  private String name;
  private String image;
  @JsonProperty("video_urls")
  private List<String> videoUrls;
  @JsonProperty("last_update")
  private String lastUpdate;

  public List<String> getMuscles() {
    return muscles;
  }

  public void setMuscles(List<String> muscles) {
    this.muscles = muscles;
  }

  private List<String> muscles;
  private List<ExerciseDescription> description;

  public List<String> getEquipments() {
    return equipments;
  }

  public void setEquipments(List<String> equipments) {
    this.equipments = equipments;
  }

  private List<String> equipments;

  public List<String> getVideoUrls() {
    return videoUrls;
  }

  public void setVideoUrls(List<String> videoUrls) {
    this.videoUrls = videoUrls;
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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<ExerciseDescription> getDescription() {
    return description;
  }

  public void setDescription(List<ExerciseDescription> description) {
    this.description = description;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

}
