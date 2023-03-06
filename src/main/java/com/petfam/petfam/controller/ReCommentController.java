package com.petfam.petfam.controller;

import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.recomment.ReCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recomments")
public class ReCommentController {

  private final ReCommentService reCommentService;


  // 대댓글 수정
  @PatchMapping("/{recommentId}")
  public ResponseEntity<String> updateReComment(@PathVariable Long recommentId,
      @RequestBody ReCommentRequestDto reCommentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK).body(
        reCommentService.updateReComment(recommentId, userDetails.getUser(), reCommentRequestDto));
  }

  // 대댓글 삭제
  @DeleteMapping("/{recommentId}")
  public ResponseEntity<String> deleteReComment(@PathVariable Long recommentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reCommentService.deleteReComment(recommentId, userDetails.getUser()));

  }

}
