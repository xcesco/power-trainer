package com.abubusoft.powertrainer.backend.model;

import org.springframework.core.io.Resource;

import java.io.Serializable;

public class ImageDto implements Serializable {
  private final String UUID;
  private final Resource resource;

  public String getUUID() {
    return UUID;
  }

  public Resource getResource() {
    return resource;
  }

  public ImageDto(String UUID, Resource resource) {
    this.UUID = UUID;
    this.resource = resource;
  }
}
