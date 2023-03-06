package com.petfam.petfam.service.recomment;

import com.petfam.petfam.dto.recomment.ReCommentRequestDto;
import com.petfam.petfam.entity.User;

public interface ReCommentService {

  // 대댓글 생성
  String reComment(Long commentId, User user, ReCommentRequestDto reCommentRequestDto);

  // 대댓글 수정
  String updateReComment(Long reCommentId, User user,
      ReCommentRequestDto reCommentRequestDto);

  // 대댓글 삭제
  String deleteReComment(Long reCommentId, User user);

}
