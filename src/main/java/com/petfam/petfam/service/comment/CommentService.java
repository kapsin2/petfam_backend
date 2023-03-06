package com.petfam.petfam.service.comment;

import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.entity.User;

public interface CommentService {

  // 댓글 생성
  String comment(Long postId, User user, CommentRequestDto commentRequestDto);

  // 댓글 수정
  String updateComment(Long commentId, User user, CommentRequestDto commentRequestDto);

  // 댓글 삭제
  String deleteComment(Long commentId, User user);
}
