package com.petfam.petfam.service.comment;

import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.repository.CommentRepository;
import com.petfam.petfam.repository.PostRepository;
import com.petfam.petfam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  // 댓글 생성
  @Transactional
  public String comment(Long postId, User user, CommentRequestDto commentRequestDto) {
    Post post = _getPost(postId);
    user = _getUser(user.getUsername());
    Comment comment = new Comment(post, user, commentRequestDto);
    commentRepository.save(comment);
    return "댓글 생성이 완료되었습니다.";
  }

  // 댓글 수정
  @Transactional
  public String updateComment(Long commentId, User user,
      CommentRequestDto commentRequestDto) {
    Comment comment = _getComment(commentId);

    user = _getUser(user.getUsername());
      if (!user.isAdmin()) {
          if (comment.getUser().getUsername().equals(user.getUsername())) {
              comment.updateComment(commentRequestDto.getContent());
              commentRepository.save(comment);
          } else {
              throw new IllegalArgumentException("자신이 작성한 댓글만 수정이 가능합니다.");
          }
    } else {
      comment.updateComment(commentRequestDto.getContent());
    }
    return "댓글 수정이 완료되었습니다.";
  }

  // 댓글 삭제
  @Transactional

  public String deleteComment(Long commentId, User user) {
    Comment comment = _getComment(commentId);
    user = _getUser(user.getUsername());
    if (!user.isAdmin()) {
        if (comment.getUser().getUsername().equals(user.getUsername())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제가 가능합니다.");
        }
    } else {
      commentRepository.deleteById(commentId);
    }
    return "댓글 삭제가 완료되었습니다.";
  }

  private Post _getPost(Long postId) {
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
    );
    return post;
  }

  private User _getUser(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
    );
    return user;
  }

  private Comment _getComment(Long commentId) {
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
    );
    return comment;
  }
}
