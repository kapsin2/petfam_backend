package com.petfam.petfam.service.recomment;

import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.ReComment;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.repository.CommentRepository;
import com.petfam.petfam.repository.ReCommentRepository;
import com.petfam.petfam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReCommentServiceImpl implements ReCommentService {

  private final UserRepository userRepository;

  private final CommentRepository commentRepository;

  private final ReCommentRepository reCommentRepository;

  // 대댓글 생성
  @Transactional
  public String reComment(Long commentId, User user,
      ReCommentRequestDto reCommentRequestDto) {
    user = _getUser(user.getUsername());
    Comment comment = _getComment(commentId);
    ReComment reComment = new ReComment(comment, user, reCommentRequestDto);
    reCommentRepository.save(reComment);
    return "댓글 생성이 완료되었습니다.";
  }

  // 대댓글 수정
  @Transactional
  public String updateReComment(Long reCommentId, User user,
      ReCommentRequestDto reCommentRequestDto) {
    ReComment reComment = _getReComment(reCommentId);
    user = _getUser(user.getUsername());
      if (!user.isAdmin()) {
          if (reComment.getUser().getUsername().equals(user.getUsername())) {
              reComment.updateReComment(reCommentRequestDto.getContent());
              reCommentRepository.save(reComment);
          } else {
              throw new IllegalArgumentException("자신이 작성한 댓글만 수정이 가능합니다.");
          }
      } else {
          reComment.updateReComment(reCommentRequestDto.getContent());
      }
    return "댓글 수정이 완료되었습니다.";
  }

  // 대댓글 삭제
  @Transactional
  public String deleteReComment(Long reCommentId, User user) {
    ReComment reComment = _getReComment(reCommentId);
    user = _getUser(user.getUsername());
      if (!user.isAdmin()) {
          if (reComment.getUser().getUsername().equals(user.getUsername())) {
              reCommentRepository.deleteById(reCommentId);
          } else {
              throw new IllegalArgumentException("자신이 작성한 댓글만 삭제가 가능합니다.");
          }
      } else {
          reCommentRepository.deleteById(reCommentId);
      }
    return "댓글 삭제가 완료되었습니다.";
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

  private ReComment _getReComment(Long reCommentId) {
    ReComment reComment = reCommentRepository.findById(reCommentId).orElseThrow(
        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
    );
    return reComment;
  }

}
