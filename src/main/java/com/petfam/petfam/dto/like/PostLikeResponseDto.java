package com.petfam.petfam.dto.like;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeResponseDto {

  private final String msg;

  private final int statuscode;

  public PostLikeResponseDto(String msg, int statuscode) {
    this.msg = msg;
    this.statuscode = statuscode;
  }
}
