package com.petfam.petfam.controller;

import com.petfam.petfam.dto.message.MessageRequestDto;
import com.petfam.petfam.dto.message.MessageResponseDto;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.message.TalkService;
import com.petfam.petfam.service.message.TalkServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/talk")
public class TalkController {

  private final TalkService talkService;

  //메시지 전송
  @PostMapping("/{receiveId}")
  public ResponseEntity<List<MessageResponseDto>> sendMessages(@PathVariable Long receiveId,
      @RequestBody MessageRequestDto messageRequestDto, @AuthenticationPrincipal
  UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(talkService.sendMessage(receiveId, userDetails.getUser(), messageRequestDto));
  }

  //전체메세지조회
  @GetMapping("/{talkId}")
  public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long talkId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(talkService.getMessages(talkId, userDetails.getUser()));
  }


}
