package com.petfam.petfam.dto.post;

import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.enums.CategoryEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AllPostResponseDto {

  private Long id;
  private String writer;
  private String title;
  private String image;
  private Integer likes;
  private CategoryEnum category;
  private LocalDateTime createdAt;
  private int view;

  public AllPostResponseDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.image = post.getImage();
    this.writer = post.getUser().getNickname();
    this.likes = post.getLikes();
    this.category = post.getCategory();
    this.createdAt = post.getCreatedAt();
    this.view = post.getView();
  }

}
