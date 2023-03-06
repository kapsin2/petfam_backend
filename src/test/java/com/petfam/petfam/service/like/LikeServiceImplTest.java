package com.petfam.petfam.service.like;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import com.petfam.petfam.dto.like.CommentLikeResponseDto;
import com.petfam.petfam.dto.like.PostLikeResponseDto;
import com.petfam.petfam.dto.like.ReCommentLikeResponseDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.Likes;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.ReComment;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.LikeEnum;
import com.petfam.petfam.repository.CommentRepository;
import com.petfam.petfam.repository.LikeRepository;
import com.petfam.petfam.repository.PostRepository;
import com.petfam.petfam.repository.ReCommentRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

  @Mock
  LikeRepository likeRepository;

  @InjectMocks
  LikeServiceImpl likeService;

  @Mock
  PostRepository postRepository;

  @Mock
  CommentRepository commentRepository;

  @Mock
  ReCommentRepository reCommentRepository;

  @Nested
  @DisplayName("게시글 좋아요")
  class LikePost {

    @Test
    @DisplayName("처음 누를 때")
    void firstPostLike() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      Likes likes = null;

      given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

      // when
      PostLikeResponseDto responseDto = likeService.likePost(post.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 누르셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("한번 더 눌러서 취소")
    void cancelPostLike() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      Likes likelog = Likes.builder().user(user).type(LikeEnum.POST).targetId(post.getId()).build();

      given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.POST, user.getId(),
          post.getId())).willReturn(Optional.of(likelog));

      // when
      PostLikeResponseDto responseDto = likeService.likePost(post.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 취소하셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 좋아요를 누를 때")
    void LikePostThatIsNotExist() {
      // given
      Post post = mock(Post.class);
      User user = mock(User.class);
      Likes likelog = Likes.builder().user(user).targetId(post.getId()).type(LikeEnum.POST).build();

      lenient().when(postRepository.findById(2L)).thenReturn(Optional.empty());
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.POST, user.getId(),
          post.getId())).willReturn(Optional.of(likelog));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> likeService.likePost(post.getId(), user));
      assertEquals("해당 게시글이 존재하지 않습니다.", exception.getMessage());
    }
  }


  @Nested
  @DisplayName("댓글 좋아요")
  class commentLike {

    @Test
    @DisplayName("처음 누를 때")
    void firstCommentLike() {
      // given
      Comment comment = mock(Comment.class);
      User user = mock(User.class);
      Likes likes = null;

      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

      // when
      CommentLikeResponseDto responseDto = likeService.likeComment(comment.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 누르셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("한번 더 눌러서 취소")
    void cancelCommentLike() {
      // given
      User user = mock(User.class);
      Comment comment = mock(Comment.class);
      Likes likelog = Likes.builder().user(user).type(LikeEnum.COMMENT).targetId(comment.getId())
          .build();

      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.COMMENT, user.getId(),
          comment.getId())).willReturn(Optional.of(likelog));

      // when
      CommentLikeResponseDto responseDto = likeService.likeComment(comment.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 취소하셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 좋아요를 누를 때")
    void LikeCommentThatIsNotExist() {
      // given
      Comment comment = mock(Comment.class);
      User user = mock(User.class);
      Likes likelog = Likes.builder().user(user).targetId(comment.getId()).type(LikeEnum.COMMENT)
          .build();

      lenient().when(commentRepository.findById(2L)).thenReturn(Optional.empty());
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.COMMENT, user.getId(),
          comment.getId())).willReturn(Optional.of(likelog));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> likeService.likeComment(comment.getId(), user));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("대댓글 좋아요")
  class reCommentLike {

    @Test
    @DisplayName("처음 누를 때")
    void firstReCommentLike() {
      // given
      User user = mock(User.class);
      ReComment reComment = mock(ReComment.class);
      Likes likes = null;

      given(reCommentRepository.findById(reComment.getId())).willReturn(Optional.of(reComment));

      // when
      ReCommentLikeResponseDto responseDto = likeService.likeReComment(reComment.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 누르셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("한번 더 눌러서 취소")
    void cancelReCommentLike() {
      // given
      User user = mock(User.class);
      ReComment reComment = mock(ReComment.class);
      Likes likelog = Likes.builder().user(user).type(LikeEnum.RECOMMENT)
          .targetId(reComment.getId())
          .build();

      given(reCommentRepository.findById(reComment.getId())).willReturn(Optional.of(reComment));
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.RECOMMENT, user.getId(),
          reComment.getId())).willReturn(Optional.of(likelog));

      // when
      ReCommentLikeResponseDto responseDto = likeService.likeReComment(reComment.getId(), user);

      // then
      assertThat(responseDto.getMsg()).isEqualTo("좋아요를 취소하셨습니다.");
      assertThat(responseDto.getStatuscode()).isEqualTo(200);
    }

    @Test
    @DisplayName("존재하지 않는 대댓글에 좋아요를 누를 때")
    void LikeReCommentThatIsNotExist() {
      // given
      ReComment reComment = mock(ReComment.class);
      User user = mock(User.class);
      Likes likelog = Likes.builder().user(user).targetId(reComment.getId())
          .type(LikeEnum.RECOMMENT)
          .build();

      lenient().when(reCommentRepository.findById(2L)).thenReturn(Optional.empty());
      given(likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.RECOMMENT, user.getId(),
          reComment.getId())).willReturn(Optional.of(likelog));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> likeService.likeReComment(reComment.getId(), user));
      assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }
  }

}