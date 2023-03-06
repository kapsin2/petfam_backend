package com.petfam.petfam.dto.post;


import com.petfam.petfam.dto.CategoryDto;
import com.petfam.petfam.entity.Post;
import lombok.Getter;

@Getter
public class PostUpdateResponseDto {

  private String title;
  private String content;
  private String image;
  private CategoryDto category;

  public PostUpdateResponseDto(Post post) {
    this.title = post.getTitle();
    this.content = post.getContent();
    this.image = post.getImage();
    this.category = new CategoryDto(post.getCategory());
  }
}
