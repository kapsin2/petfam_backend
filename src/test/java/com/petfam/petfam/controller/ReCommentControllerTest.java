package com.petfam.petfam.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.recomment.ReCommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class ReCommentControllerTest {

  @Mock
  private ReCommentServiceImpl reCommentService;

  @Test
  @DisplayName("대댓글 수정")
  void updateReComment() {
    // given
    Long reCommentId = 123L;
    ReCommentRequestDto reCommentRequestDto = ReCommentRequestDto.builder()
        .content("content")
        .build();
    User mockUser = User.builder()
        .username("User")
        .password("1234")
        .nickname("Nickname")
        .image("image")
        .userRole(UserRoleEnum.USER)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(mockUser, "User");

    given(reCommentService.updateReComment(eq(reCommentId), eq(mockUser), eq(reCommentRequestDto)))
        .willReturn("Success");

    ReCommentController reCommentController = new ReCommentController(reCommentService);

    // when
    ResponseEntity<String> response = reCommentController.updateReComment(reCommentId, reCommentRequestDto, userDetails);

    // then
    verify(reCommentService).updateReComment(eq(reCommentId), eq(mockUser), eq(reCommentRequestDto));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody());
  }
  @Test
  @DisplayName("대댓글 삭제")
  void deleteReComment() {
    // given
    Long reCommentId = 123L;
    User mockUser = User.builder()
        .username("User")
        .password("1234")
        .nickname("Nickname")
        .image("image")
        .userRole(UserRoleEnum.USER)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(mockUser, "User");

    given(reCommentService.deleteReComment(eq(reCommentId), eq(mockUser))).willReturn("Success");
    ReCommentController reCommentController = new ReCommentController(reCommentService);

    // when
    ResponseEntity<String> response = reCommentController.deleteReComment(reCommentId, userDetails);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Success", response.getBody());
    verify(reCommentService).deleteReComment(eq(reCommentId), eq(mockUser));
  }
}