package com.petfam.petfam.dto.recomment;

import com.petfam.petfam.entity.ReComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReCommentResponseDto {

  private Long id;
  private String writer;
  private String content;
  private LocalDateTime createdAt;
  private Integer likes;

  public ReCommentResponseDto(ReComment recomment) {
    this.id = recomment.getId();
    this.writer = recomment.getUser().getNickname();
    this.content = recomment.getContent();
    this.likes = recomment.getLikes();
    this.createdAt = recomment.getCreatedAt();
  }
}
