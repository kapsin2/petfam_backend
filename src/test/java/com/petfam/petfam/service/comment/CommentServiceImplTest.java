package com.petfam.petfam.service.comment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.repository.CommentRepository;
import com.petfam.petfam.repository.PostRepository;
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
class CommentServiceImplTest {

  @Mock
  CommentRepository commentRepository;

  @Mock
  PostRepository postRepository;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  CommentServiceImpl commentService;

  @Nested
  @DisplayName("댓글 생성")
  class createComment {

    @Test
    @DisplayName("성공")
    void success() {

      Post post = mock(Post.class);
      User user = mock(User.class);

      CommentRequestDto requestDto = CommentRequestDto.builder().content("content").build();

      Comment comment = new Comment(post, user, requestDto);

      given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

      // when
      String savedComment = commentService.comment(post.getId(), user, requestDto);

      // then
      assertThat(savedComment).isEqualTo("댓글 생성이 완료되었습니다.");
    }

    @Test
    @DisplayName("댓글을 달 게시글이 없는 경우")
    void noExistPost() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      CommentRequestDto requestDto = mock(CommentRequestDto.class);
      Comment comment = new Comment(post, user, requestDto);

      lenient().when(postRepository.findById(2L)).thenReturn(Optional.empty());

      // when & then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> commentService.comment(post.getId(), user, requestDto));
      assertEquals("해당 게시글이 존재하지 않습니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("댓글 수정")
  class updateComment {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      given(user.getUsername()).willReturn("user");

      CommentRequestDto requestDto = mock(CommentRequestDto.class);

      Comment comment = new Comment(post, user, requestDto);

      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

      CommentRequestDto updateRequestDto = CommentRequestDto.builder().content("content").build();

      // when
      commentService.updateComment(comment.getId(), user, updateRequestDto);

      // then
      assertThat(updateRequestDto.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    @DisplayName("관리자가 댓글 수정")
    void adminUpdateComment() {
      // Given
      Post post = mock(Post.class);
      User user = mock(User.class);
      User adminUser = new User("admin", "pass", "admin", "image", UserRoleEnum.ADMIN);
      CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("content").build();
      Comment comment = new Comment(post, user, commentRequestDto);
      commentRepository.save(comment);

      CommentRequestDto updateComment = CommentRequestDto.builder().content("updateContent")
          .build();

      when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
      when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(
          Optional.of(adminUser));

      // When
      String result = commentService.updateComment(comment.getId(), adminUser, updateComment);

      // Then
      assertThat(result).isEqualTo("댓글 수정이 완료되었습니다.");
      assertThat(commentRepository.findById(comment.getId()).orElseThrow().getContent()).isEqualTo(
          "updateContent");
    }

    @Test
    @DisplayName("존재하지 않는 댓글의 수정을 시도")
    void updateCommentThatIsNotExist() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      CommentRequestDto requestDto = CommentRequestDto.builder().content("content").build();
      Comment comment = new Comment(post, user, requestDto);

      lenient().when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
      CommentRequestDto updateRequestDto = CommentRequestDto.builder().content("content1").build();

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> commentService.updateComment(2L, user, updateRequestDto));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 작성자와 다른 유저가 댓글 수정을 시도할 경우")
    void updateCommentAuthCheck() {
      // given
      Post post = mock(Post.class);
      User user1 = mock(User.class);
      given(user1.getUsername()).willReturn("user1");
      User user2 = mock(User.class);
      given(user2.getUsername()).willReturn("user2");
      CommentRequestDto requestDto = CommentRequestDto.builder().content("content").build();
      Comment comment = new Comment(post, user1, requestDto);

      lenient().when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
      CommentRequestDto updateRequestDto = CommentRequestDto.builder().content("content1").build();
      lenient().when(userRepository.findByUsername(user1.getUsername()))
          .thenReturn(Optional.of(user1));
      lenient().when(userRepository.findByUsername(user2.getUsername()))
          .thenReturn(Optional.of(user2));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> commentService.updateComment(comment.getId(), user2, updateRequestDto));
      assertEquals("자신이 작성한 댓글만 수정이 가능합니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("댓글 삭제")
  class deleteComment {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      given(user.getUsername()).willReturn("user");
      CommentRequestDto requestDto = mock(CommentRequestDto.class);
      Comment comment = new Comment(post, user, requestDto);

      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
      given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

      // when
      commentService.deleteComment(comment.getId(), user);

      // then
      verify(commentRepository, times(1)).deleteById(comment.getId());
    }

    @Test
    @DisplayName("관리자가 댓글 삭제")
    void adminDeleteComment() {
      // Given
      Post post = mock(Post.class);
      User user = mock(User.class);
      User adminUser = new User("admin", "pass", "admin", "image", UserRoleEnum.ADMIN);
      Comment comment = mock(Comment.class);
      commentRepository.save(comment);

      when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
      when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(
          Optional.of(adminUser));

      // When
      String result = commentService.deleteComment(comment.getId(), adminUser);

      // Then
      assertThat(result).isEqualTo("댓글 삭제가 완료되었습니다.");
      verify(commentRepository, times(1)).deleteById(comment.getId());
    }

    @Test
    @DisplayName("존재하지 않는 댓글의 삭제를 시도")
    void deleteCommentThatIsNotExist() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      CommentRequestDto requestDto = CommentRequestDto.builder().content("content").build();
      Comment comment = new Comment(post, user, requestDto);

      lenient().when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> commentService.deleteComment(2L, user));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 작성자와 다른 유저가 댓글 삭제를 시도할 경우")
    void updateCommentAuthCheck() {
      // given
      Post post = mock(Post.class);

      User user1 = mock(User.class);
      given(user1.getUsername()).willReturn("user1");
      User user2 = mock(User.class);
      given(user2.getUsername()).willReturn("user2");

      CommentRequestDto requestDto = mock(CommentRequestDto.class);
      Comment comment = new Comment(post, user1, requestDto);

      lenient().when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
      lenient().when(userRepository.findByUsername(user1.getUsername()))
          .thenReturn(Optional.of(user1));
      lenient().when(userRepository.findByUsername(user2.getUsername()))
          .thenReturn(Optional.of(user2));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> commentService.deleteComment(comment.getId(), user2));
      assertEquals("본인이 작성한 댓글만 삭제가 가능합니다.", exception.getMessage());
    }
  }
}