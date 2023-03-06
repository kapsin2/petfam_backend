package com.petfam.petfam.controller;

import com.petfam.petfam.dto.like.CommentLikeResponseDto;
import com.petfam.petfam.dto.like.PostLikeResponseDto;
import com.petfam.petfam.dto.like.ReCommentLikeResponseDto;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  // 게시글 좋아요
  @PostMapping("/posts/{postId}/like")
  public ResponseEntity<PostLikeResponseDto> likePost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    PostLikeResponseDto response = likeService.likePost(postId, userDetails.getUser());
    return ResponseEntity.ok(response);
  }

  // 댓글 좋아요
  @PostMapping("/comments/{commentId}/like")
  public ResponseEntity<CommentLikeResponseDto> likeComment(@PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    CommentLikeResponseDto response = likeService.likeComment(commentId, userDetails.getUser());
    return ResponseEntity.ok(response);
  }

  // 대댓글 좋아요
  @PostMapping("/recomments/{recommentId}/like")
  public ResponseEntity<ReCommentLikeResponseDto> likeReComment(@PathVariable Long recommentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReCommentLikeResponseDto response = likeService.likeReComment(recommentId, userDetails.getUser());
    return ResponseEntity.ok(response);
  }
}
