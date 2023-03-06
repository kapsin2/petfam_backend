package com.petfam.petfam.dto.comment;

import com.petfam.petfam.dto.recomment.ReCommentResponseDto;
import com.petfam.petfam.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private Long id;
  private String writer;
  private String content;
  private Integer likes;
  private LocalDateTime createdAt;
  private List<ReCommentResponseDto> reComments;

  public CommentResponseDto(Comment comment) {
    this.id = comment.getId();
    this.writer = comment.getUser().getNickname();
    this.content = comment.getContent();
    this.likes = comment.getLikes();
    this.createdAt = comment.getCreatedAt();
    this.reComments = comment.getReComment().stream().map(ReCommentResponseDto::new)
        .collect(Collectors.toList());
  }
}
