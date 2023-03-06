package com.petfam.petfam.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.comment.CommentServiceImpl;
import com.petfam.petfam.service.recomment.ReCommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

  @InjectMocks
  private CommentController commentController;

  @Mock
  private CommentServiceImpl commentService;

  @Mock
  private UserDetailsImpl userDetails;

  @Mock
  private ReCommentServiceImpl reCommentService;


  @Test
  @DisplayName("댓글 수정")
  void updateComment() {
    // given
    Long commentId = 1L;
    User user = User.builder()
        .id(2L)
        .username("user")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();
    Comment comment = Comment.builder()
        .id(commentId)
        .user(user)
        .content("old content")
        .build();
    CommentRequestDto commentRequestDto = CommentRequestDto.builder()
        .content("댓글 수정")
        .build();

    when(commentService.updateComment(commentId, user, commentRequestDto)).thenReturn("댓글 수정");
    when(userDetails.getUser()).thenReturn(user);

    // when
    ResponseEntity<String> response = commentController.updateComment(commentId, commentRequestDto, userDetails);

    // then
    verify(commentService, times(1)).updateComment(commentId, user, commentRequestDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("댓글 수정", response.getBody());
    verify(userDetails, times(1)).getUser();
    assertEquals(commentRequestDto.getContent(), response.getBody());
  }


  @Test
  @DisplayName("댓글삭제")
  void deleteComment() {
    // given
    Long commentId = 1L;
    User user = User.builder()
        .id(4L)
        .username("user")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();
    Comment comment = Comment.builder()
        .id(commentId)
        .user(user)
        .content("comment content")
        .build();

    when(commentService.deleteComment(commentId, user)).thenReturn("댓글 삭제");
    when(userDetails.getUser()).thenReturn(user);

    // when
    ResponseEntity<String> response = commentController.deleteComment(commentId, userDetails);

    // then
    verify(commentService, times(1)).deleteComment(commentId, user);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("댓글 삭제", response.getBody());
    verify(userDetails, times(1)).getUser();
  }


  @Test
  @DisplayName("대댓글 작성")
  void reComment() {
     // given
    Long commentId = 1L;
    ReCommentRequestDto reCommentRequestDto = ReCommentRequestDto.builder().content("recomment").build();
    User user = User.builder()
        .id(4L)
        .username("user")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();

    when(userDetails.getUser()).thenReturn(user);
      when(reCommentService.reComment(commentId, user, reCommentRequestDto)).thenReturn("success");

      // when
      ResponseEntity<String> responseEntity = commentController.reComment(commentId,
          reCommentRequestDto, userDetails);

      // then
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertEquals("success", responseEntity.getBody());
      verify(userDetails, times(1)).getUser();
      verify(reCommentService, times(1)).reComment(commentId, user, reCommentRequestDto);
    }
  }

