package com.petfam.petfam.dto.message;

import com.petfam.petfam.entity.Message;
import lombok.Getter;

@Getter
public class MessageResponseDto {

  private String writer;
  private String content;

  public MessageResponseDto(Message message) {
    this.writer = message.getWriter();
    this.content = message.getContent();
  }


}
