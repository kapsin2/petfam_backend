package com.petfam.petfam.entity.enums;


import lombok.Getter;

@Getter
public enum CategoryEnum {

  PET("펫펨 자랑"),
  CHAT("펫팸 잡담"),
  QNA("Q&A");

  private final String name;

  CategoryEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
