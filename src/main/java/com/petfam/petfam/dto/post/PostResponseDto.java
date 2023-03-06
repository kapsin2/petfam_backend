package com.petfam.petfam.dto.post;

import com.petfam.petfam.dto.CategoryDto;
import com.petfam.petfam.dto.comment.CommentResponseDto;
import com.petfam.petfam.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

  private Long id;
  private String title;
  private String image;
  private String writer;
  private Long writerId;
  private Integer likes;
  private String content;
  private CategoryDto category;
  private List<CommentResponseDto> comments; // 리스트

  //테스트 코드
  @Builder
  public PostResponseDto(Long id, String title, String image, String writer, Long writerId,
      Integer likes, String content, CategoryDto category,
      List<CommentResponseDto> comments) {
    this.id = id;
    this.title = title;
    this.image = image;
    this.writer = writer;
    this.writerId = writerId;
    this.likes = likes;
    this.content = content;
    this.category = category;
    this.comments = comments;
  }

  public PostResponseDto(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.image = post.getImage();
    this.content = post.getContent();
    this.writer = post.getUser().getNickname();
    this.writerId = post.getUser().getId();
    this.likes = post.getLikes();
    this.category = new CategoryDto(post.getCategory());
    this.comments = post.getComments().stream().map(CommentResponseDto::new)
        .collect(Collectors.toList());
  }
}