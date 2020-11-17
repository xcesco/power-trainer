package com.abubusoft.powertrainer.backend.repositories.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Entity
@Table(name = "pt_exercises",
        indexes = {
                @Index(name = "pt_exercises_index_uuid", columnList = "UUID", unique = true)
        })
public class Exercise {
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private long id;
  private String UUID;
  private String name;
  private String image;

  @Lob
  @Basic
  @JsonProperty("video_urls")
  private ArrayList<String> videoUrls = new ArrayList<>();
  @JsonProperty("last_update")
  private String lastUpdate;

  @Lob
  @Column
  @Basic
  private ArrayList<String> muscles = new ArrayList<>();

  @Lob
  @Basic
  private LinkedHashMap<LanguageType, String> descriptions = new LinkedHashMap<>();

  @Lob
  @Basic
  private ArrayList<String> equipments = new ArrayList<>();

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

  public ArrayList<String> getVideoUrls() {
    return videoUrls;
  }

  public void setVideoUrls(ArrayList<String> videoUrls) {
    this.videoUrls = videoUrls;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public ArrayList<String> getMuscles() {
    return muscles;
  }

  public void setMuscles(ArrayList<String> muscles) {
    this.muscles = muscles;
  }

  public LinkedHashMap<LanguageType, String> getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(LinkedHashMap<LanguageType, String> description) {
    this.descriptions = description;
  }

  public ArrayList<String> getEquipments() {
    return equipments;
  }

  public void setEquipments(ArrayList<String> equipments) {
    this.equipments = equipments;
  }

}
