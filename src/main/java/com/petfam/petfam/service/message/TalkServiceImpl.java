package com.petfam.petfam.service.message;

import com.petfam.petfam.dto.message.MessageRequestDto;
import com.petfam.petfam.dto.message.MessageResponseDto;
import com.petfam.petfam.entity.Message;
import com.petfam.petfam.entity.Talk;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.repository.MessageRepository;
import com.petfam.petfam.repository.TalkRepository;
import com.petfam.petfam.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TalkServiceImpl implements TalkService {

  private final TalkRepository talkRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public List<MessageResponseDto> sendMessage(Long receiveId, User user,
      MessageRequestDto messageRequestDto) {
    if(userRepository.findById(receiveId).isEmpty()) {throw new IllegalArgumentException("해당유저가 존재하지 않습니다.");}

    Optional<Talk> talkCk = talkRepository.findByApplyIdAndReceiveId(user.getId(), receiveId);
    Optional<Talk> talkCk2 = talkRepository.findByApplyIdAndReceiveId(receiveId, user.getId());

    if (talkCk.isEmpty() && talkCk2.isEmpty()) {
      Talk talk = new Talk(user.getId(), receiveId);
      talkRepository.save(talk);
      Message message = new Message(talk.getId(), user.getNickname(), messageRequestDto);
      messageRepository.save(message);

      return getMessages(talk.getId(), user);
    } else if(talkCk.isPresent()){

      Talk talk = _findTalk(user.getId(),receiveId);
      Message message = new Message(talk.getId(), user.getNickname(), messageRequestDto);
      messageRepository.save(message);

      return getMessages(talk.getId(), user);
    } else {
      Talk talk = _findTalk(receiveId, user.getId());
      Message message = new Message(talk.getId(), user.getNickname(), messageRequestDto);
      messageRepository.save(message);

      return getMessages(talk.getId(), user);
    }

  }

  @Override
  @Transactional(readOnly = true)
  public List<MessageResponseDto> getMessages(Long talkId, User user) {
    Talk talk = _findTalk(talkId);
    if ((!talk.getApplyId().equals(user.getId())) && (!talk.getReceiveId().equals(user.getId()))) {
      throw new IllegalArgumentException("해당 톡방에 접근권한이 없습니다.");
    }

    List<Message> messages = messageRepository.findAllByTalkId(talkId);
    List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
    for (Message message : messages) {
      messageResponseDtos.add(new MessageResponseDto(message));
    }
    return messageResponseDtos;
  }

  private Talk _findTalk(Long applyId, Long receiveId) {
    return talkRepository.findByApplyIdAndReceiveId(applyId, receiveId).orElseThrow(
        () -> new IllegalArgumentException("톡방이 존재하지 않습니다.")
    );
  }

  private Talk _findTalk(Long talkId) {
    return talkRepository.findById(talkId).orElseThrow(
        () -> new IllegalArgumentException("톡방이 존재하지 않습니다.")
    );
  }
}
