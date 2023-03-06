package com.petfam.petfam.controller;

import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.comment.CommentService;
import com.petfam.petfam.service.recomment.ReCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  private final ReCommentService reCommentService;

  // 댓글 수정
  @PatchMapping("/{commentId}")
  public ResponseEntity<String> updateComment(@PathVariable Long commentId,
      @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.updateComment(commentId, userDetails.getUser(), commentRequestDto));
  }

  // 댓글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.deleteComment(commentId, userDetails.getUser()));
  }

  // 대댓글 생성
  @PostMapping("{commentId}/recomments")
  public ResponseEntity<String> reComment(@PathVariable Long commentId,
      @RequestBody ReCommentRequestDto reCommentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reCommentService.reComment(commentId, userDetails.getUser(), reCommentRequestDto));

  }
}
