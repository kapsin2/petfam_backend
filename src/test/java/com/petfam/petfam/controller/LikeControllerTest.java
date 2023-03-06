package com.petfam.petfam.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.petfam.petfam.dto.like.CommentLikeResponseDto;
import com.petfam.petfam.dto.like.PostLikeResponseDto;
import com.petfam.petfam.dto.like.ReCommentLikeResponseDto;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.like.LikeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

  @InjectMocks
  private LikeController likeController;

  @Mock
  private LikeServiceImpl likeService;

  @Test
  @DisplayName("게시물 좋아요")
  void likePost() {
    // given
    Long postId = 1L;
    User mockUser = User.builder().username("TestUser").build();
    UserDetailsImpl userDetails = new UserDetailsImpl(mockUser, "TestUser");
    PostLikeResponseDto mockResponse = PostLikeResponseDto.builder()
        .msg("Success")
        .statuscode(1)
        .build();
    when(likeService.likePost(eq(postId), eq(mockUser)))
        .thenReturn(mockResponse);

    // when
    ResponseEntity<PostLikeResponseDto> responseEntity = likeController.likePost(postId,
        userDetails);

    // then
    verify(likeService).likePost(eq(postId), eq(mockUser));
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(mockResponse, responseEntity.getBody());
    assertEquals(1, responseEntity.getBody().getStatuscode());
  }

  @Test
  @DisplayName("댓글 좋아요")
  void likeComment() {
    // given
    Long commentId = 2L;
    User mockUser = User.builder().username("TestUser").build();
    UserDetailsImpl userDetails = new UserDetailsImpl(mockUser, "TestUser");
    CommentLikeResponseDto mockResponse = CommentLikeResponseDto.builder()
        .msg("Success")
        .statuscode(1)
        .build();

    // when
    when(likeService.likeComment(eq(commentId), eq(mockUser)))
        .thenReturn(mockResponse);

    LikeController likeController = new LikeController(likeService);
    ResponseEntity<CommentLikeResponseDto> response = likeController.likeComment(commentId,
        userDetails);

    // then
    assertAll(
        () -> verify(likeService, times(1)).likeComment(eq(commentId), eq(mockUser)),
        () -> assertEquals(mockResponse, response.getBody()),
        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        () -> assertEquals(1, response.getBody().getStatuscode())
    );
  }
  @Test
  @DisplayName("대댓글 좋아요")
  void likeReComment() {
    // given
    Long recommentId = 3L;
    User mockUser = User.builder().username("TestUser").build();
    UserDetailsImpl userDetails = new UserDetailsImpl(mockUser, "TestUser");
    ReCommentLikeResponseDto mockResponse = ReCommentLikeResponseDto.builder()
        .msg("Success")
        .statuscode(1)
        .build();
    when(likeService.likeReComment(eq(recommentId), eq(mockUser))).thenReturn(mockResponse);
    LikeController likeController = new LikeController(likeService);

    // when
    ResponseEntity<ReCommentLikeResponseDto> responseEntity = likeController.likeReComment(recommentId, userDetails);
    ReCommentLikeResponseDto response = responseEntity.getBody();

    // then
    verify(likeService).likeReComment(eq(recommentId), eq(mockUser));
    assertEquals(mockResponse, response);
    assertEquals(1, response.getStatuscode());
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
