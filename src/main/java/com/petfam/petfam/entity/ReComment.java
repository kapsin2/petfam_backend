package com.petfam.petfam.entity;


import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReComment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String content;

  @Column
  private Integer likes;


  @JoinColumn
  @ManyToOne
  private User user;

  @JoinColumn
  @ManyToOne
  private Comment comment;


  public void updateLike(boolean islike) {
    likes += islike ? 1 : -1;
    if (likes < 0) {
      likes = 0;
    }
  }

  @Builder
  public ReComment(Comment comment, User user, ReCommentRequestDto reCommentRequestDto) {
    this.comment = comment;
    this.user = user;
    this.content = reCommentRequestDto.getContent();
    this.likes = 0;
  }

  public void updateReComment(String content) {
    this.content = content;
  }
}
