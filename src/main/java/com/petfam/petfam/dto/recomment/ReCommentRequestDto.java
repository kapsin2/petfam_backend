package com.petfam.petfam.dto.recomment;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = ReCommentRequestDto.ReCommentRequestDtoBuilder.class)
public class ReCommentRequestDto {

  private String content;

  public static class ReCommentRequestDtoBuilder {

    @JsonSetter("content")
    private String content;

    public ReCommentRequestDto build() {
      return new ReCommentRequestDto(content);
    }
  }
}
