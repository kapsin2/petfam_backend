package com.petfam.petfam.entity;


import com.petfam.petfam.dto.comment.CommentRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @JoinColumn
  @ManyToOne
  private User user;

  @JoinColumn
  @ManyToOne
  private Post post;

  @Column
  private Integer likes;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReComment> reComment = new ArrayList<>();

  //테스트 코드
  @Builder
  public Comment(Long id, String content, User user, Post post, Integer likes) {
    this.id = id;
    this.content = content;
    this.user = user;
    this.post = post;
    this.likes = likes;
  }

  public void updateLike(boolean islike) {
    likes += islike ? 1 : -1;
    if (likes < 0) {
      likes = 0;
    }
  }

  public Comment(Post post, User user, CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
    this.user = user;
    this.post = post;
    this.likes = 0;
  }

  public void updateComment(String content) {
    this.content = content;
  }

}
