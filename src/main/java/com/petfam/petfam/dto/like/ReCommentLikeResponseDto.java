package com.petfam.petfam.dto.like;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReCommentLikeResponseDto {

  private final String msg;

  private final int statuscode;

  public ReCommentLikeResponseDto(String msg, int statuscode) {
    this.msg = msg;
    this.statuscode = statuscode;
  }
}
