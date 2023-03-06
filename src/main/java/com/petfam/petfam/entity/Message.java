package com.petfam.petfam.entity;

import com.petfam.petfam.dto.message.MessageRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @JoinColumn
  @Column(nullable = false)
  private Long talkId;

  @Column(nullable = false)
  private String writer;

  @Column(nullable = false)
  private String content;

  public Message(Long talkId, String writer, MessageRequestDto messageRequestDto) {
    this.talkId = talkId;
    this.writer = writer;
    this.content = messageRequestDto.getContent();
  }
}
