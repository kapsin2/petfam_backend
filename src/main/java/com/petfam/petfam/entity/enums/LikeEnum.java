package com.petfam.petfam.entity.enums;

public enum LikeEnum {
  POST(LikeType.POST),
  COMMENT(LikeType.COMMENT),
  RECOMMENT(LikeType.RECOMMENT);

  private final String type;

  LikeEnum(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public static class LikeType {

    public static final String POST = "post";
    public static final String COMMENT = "comment";
    public static final String RECOMMENT = "recomment";
  }
}
