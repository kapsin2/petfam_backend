package com.petfam.petfam.dto.comment;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = CommentRequestDto.CommentRequestDtoBuilder.class)
public class CommentRequestDto {

  private String content;

  public static class CommentRequestDtoBuilder {

    @JsonSetter("content")
    private String content;

    public CommentRequestDto build() {
      return new CommentRequestDto(content);
    }
  }
}
