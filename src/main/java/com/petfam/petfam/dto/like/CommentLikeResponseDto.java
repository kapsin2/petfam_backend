package com.petfam.petfam.dto.like;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentLikeResponseDto {

  private final String msg;

  private final int statuscode;

  public CommentLikeResponseDto(String msg, int statuscode) {
    this.msg = msg;
    this.statuscode = statuscode;
  }
}
