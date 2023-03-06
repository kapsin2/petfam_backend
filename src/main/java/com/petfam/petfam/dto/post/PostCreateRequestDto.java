package com.petfam.petfam.dto.post;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.petfam.petfam.entity.enums.CategoryEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = PostCreateRequestDto.PostCreateRequestDtoBuilder.class)
public class PostCreateRequestDto {

  private String title;
  private String content;
  private String image;
  private CategoryEnum category;

  public static class PostCreateRequestDtoBuilder {

    @JsonSetter("title")
    private String title;
    @JsonSetter("content")
    private String content;
    @JsonSetter("image")
    private String image;
    @JsonSetter("category")
    private CategoryEnum category;

    public PostCreateRequestDto build() {
      return new PostCreateRequestDto(title, content, image, category);
    }
  }
}
