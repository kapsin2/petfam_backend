package com.petfam.petfam.service.message;

import com.petfam.petfam.dto.message.MessageRequestDto;
import com.petfam.petfam.dto.message.MessageResponseDto;
import com.petfam.petfam.entity.User;
import java.util.List;


public interface TalkService {

  public List<MessageResponseDto> sendMessage(Long receiveId, User user,
      MessageRequestDto messageRequestDto);

  public List<MessageResponseDto> getMessages(Long talkId, User user);

}
