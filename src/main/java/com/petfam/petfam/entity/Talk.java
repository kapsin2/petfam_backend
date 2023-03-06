package com.petfam.petfam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Talk {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private Long applyId;

  @Column(nullable = false)
  private Long receiveId;

  @JoinColumn
  @OneToMany
  private List<Message> messages = new ArrayList<>();

  public Talk(Long applyId, Long receiveId) {
    this.applyId = applyId;
    this.receiveId = receiveId;
  }
}
