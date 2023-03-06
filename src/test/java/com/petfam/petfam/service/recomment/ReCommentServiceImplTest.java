package com.petfam.petfam.service.recomment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.ReComment;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.repository.CommentRepository;
import com.petfam.petfam.repository.ReCommentRepository;
import com.petfam.petfam.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReCommentServiceImplTest {

  @Mock
  ReCommentRepository reCommentRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  CommentRepository commentRepository;

  @InjectMocks
  ReCommentServiceImpl reCommentService;


  @Nested
  @DisplayName("대댓글 생성")
  class createReComment {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      User user = mock(User.class);
      Comment comment = mock(Comment.class);

      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user, requestDto);

      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

      // when
      String savedReComment = reCommentService.reComment(comment.getId(), user, requestDto);

      // then
      assertThat(savedReComment).isEqualTo("댓글 생성이 완료되었습니다.");
    }

    @Test
    @DisplayName("대댓글을 달 댓글이 없는 경우")
    void noExistComment() {
      // given
      User user = mock(User.class);
      Comment comment = mock(Comment.class);

      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user, requestDto);

      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
      lenient().when(commentRepository.findById(2L)).thenReturn(Optional.empty());

      // when & then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> reCommentService.reComment(
              comment.getId(), user, requestDto));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("대댓글 수정")
  class updateReComment {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      User user = mock(User.class);
      given(user.getUsername()).willReturn("user");
      Comment comment = mock(Comment.class);

      ReCommentRequestDto requestDto = mock(ReCommentRequestDto.class);
      ReComment reComment = new ReComment(comment, user, requestDto);

      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
      given(reCommentRepository.findById(reComment.getId())).willReturn(Optional.of(reComment));

      ReCommentRequestDto updateRequestDto = ReCommentRequestDto.builder().content("content1")
          .build();

      // when
      reCommentService.updateReComment(reComment.getId(), user, updateRequestDto);

      // then
      assertThat(updateRequestDto.getContent()).isEqualTo(reComment.getContent());
    }

    @Test
    @DisplayName("관리자가 대댓글 수정")
    void adminUpdateReComment() {
      // Given
      Comment comment = mock(Comment.class);
      User user = mock(User.class);
      User adminUser = new User("admin", "pass", "admin", "image", UserRoleEnum.ADMIN);
      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = ReComment.builder().reCommentRequestDto(requestDto).build();
      reCommentRepository.save(reComment);

      ReCommentRequestDto updateReComment = ReCommentRequestDto.builder().content("updateContent")
          .build();

      when(reCommentRepository.findById(reComment.getId())).thenReturn(Optional.of(reComment));
      when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(
          Optional.of(adminUser));

      // When
      String result = reCommentService.updateReComment(reComment.getId(), adminUser,
          updateReComment);

      // Then
      assertThat(result).isEqualTo("댓글 수정이 완료되었습니다.");
      assertThat(
          reCommentRepository.findById(reComment.getId()).orElseThrow().getContent()).isEqualTo(
          "updateContent");
    }

    @Test
    @DisplayName("존재하지 않는 대댓글의 수정을 시도")
    void updateReCommentThatIsNotExist() {
      // given
      User user = mock(User.class);
      Comment comment = mock(Comment.class);
      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user, requestDto);

      lenient().when(reCommentRepository.findById(reComment.getId()))
          .thenReturn(Optional.of(reComment));
      ReCommentRequestDto updateRequestDto = ReCommentRequestDto.builder().content("content1")
          .build();

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> reCommentService.updateReComment(2L, user, updateRequestDto));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("대댓글 작성자와 다른 유저가 대댓글 수정을 시도")
    void updateReCommentAuthCheck() {
      // given
      User user1 = mock(User.class);
      given(user1.getUsername()).willReturn("user1");
      User user2 = mock(User.class);
      given(user2.getUsername()).willReturn("user2");
      Comment comment = mock(Comment.class);

      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user1, requestDto);
      ReCommentRequestDto updateRequestDto = ReCommentRequestDto.builder().content("content1")
          .build();

      lenient().when(reCommentRepository.findById(reComment.getId()))
          .thenReturn(Optional.of(reComment));
      lenient().when(userRepository.findByUsername(user1.getUsername()))
          .thenReturn(Optional.of(user1));
      lenient().when(userRepository.findByUsername(user2.getUsername()))
          .thenReturn(Optional.of(user2));

      // when & then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> reCommentService.updateReComment(reComment.getId(), user2, updateRequestDto));
      assertEquals("자신이 작성한 댓글만 수정이 가능합니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("댓글 삭제")
  class deleteReComment {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      Comment comment = mock(Comment.class);
      User user = mock(User.class);
      ReCommentRequestDto requestDto = mock(ReCommentRequestDto.class);
      ReComment reComment = new ReComment(comment, user, requestDto);

      given(user.getUsername()).willReturn("user");
      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
      given(reCommentRepository.findById(reComment.getId())).willReturn(Optional.of(reComment));

      // when
      reCommentService.deleteReComment(reComment.getId(), user);

      // then
      verify(reCommentRepository, times(1)).deleteById(reComment.getId());
    }

    @Test
    @DisplayName("관리자가 댓글 삭제")
    void adminDeleteComment() {
      // Given
      Comment comment = mock(Comment.class);
      User user = mock(User.class);
      User adminUser = new User("admin", "pass", "admin", "image", UserRoleEnum.ADMIN);
      ReComment reComment = mock(ReComment.class);
      reCommentRepository.save(reComment);

      when(reCommentRepository.findById(reComment.getId())).thenReturn(Optional.of(reComment));
      when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(
          Optional.of(adminUser));

      // When
      String result = reCommentService.deleteReComment(reComment.getId(), adminUser);

      // Then
      assertThat(result).isEqualTo("댓글 삭제가 완료되었습니다.");
      verify(reCommentRepository, times(1)).deleteById(reComment.getId());
    }

    @Test
    @DisplayName("존재하지 않는 대댓글의 삭제 시도")
    void deleteReCommentThatIsNotExist() {
      // given
      User user = mock(User.class);
      Comment comment = mock(Comment.class);
      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user, requestDto);

      lenient().when(reCommentRepository.findById(reComment.getId()))
          .thenReturn(Optional.of(reComment));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> reCommentService.deleteReComment(2L, user));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("대댓글 작성자와 다른 유저가 대댓글 삭제를 시도")
    void deleteReCommentAuthCheck() {
      // given
      User user1 = mock(User.class);
      given(user1.getUsername()).willReturn("user1");
      User user2 = mock(User.class);
      given(user2.getUsername()).willReturn("user2");
      Comment comment = mock(Comment.class);

      ReCommentRequestDto requestDto = ReCommentRequestDto.builder().content("content").build();
      ReComment reComment = new ReComment(comment, user1, requestDto);

      lenient().when(reCommentRepository.findById(reComment.getId()))
          .thenReturn(Optional.of(reComment));
      lenient().when(userRepository.findByUsername(user1.getUsername()))
          .thenReturn(Optional.of(user1));
      lenient().when(userRepository.findByUsername(user2.getUsername()))
          .thenReturn(Optional.of(user2));

      // when & then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> reCommentService.deleteReComment(reComment.getId(), user2));
      assertEquals("자신이 작성한 댓글만 삭제가 가능합니다.", exception.getMessage());
    }
  }
}