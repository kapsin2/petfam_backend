package com.petfam.petfam.entity;

import com.petfam.petfam.entity.enums.LikeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Likes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private Long targetId;

  @Column
  @Enumerated(value = EnumType.STRING)
  private LikeEnum type;

  // test를 위해 id생성자에 추가, 추후 삭제 예정
  @Builder
  public Likes(Long id, User user, Long targetId, LikeEnum type) {
    this.id = id;
    this.user = user;
    this.targetId = targetId;
    this.type = type;
  }

  @Builder
  public Likes(User user, Long targetId, LikeEnum type) {
    this.user = user;
    this.targetId = targetId;
    this.type = type;
  }

}
