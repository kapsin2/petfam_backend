package com.petfam.petfam.dto.post;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.petfam.petfam.entity.enums.CategoryEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = PostUpdateRequestDto.PostUpdateRequestDtoBuilder.class)
public class PostUpdateRequestDto {

  private String title;
  private String content;
  private String image;
  private CategoryEnum category;

  public static class PostUpdateRequestDtoBuilder {

    @JsonSetter("title")
    private String title;
    @JsonSetter("content")
    private String content;
    @JsonSetter("image")
    private String image;
    @JsonSetter("category")
    private CategoryEnum category;

    public PostUpdateRequestDto build() {
      return new PostUpdateRequestDto(title, content, image, category);
    }
  }
}


