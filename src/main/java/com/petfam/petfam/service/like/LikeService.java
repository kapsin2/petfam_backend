package com.petfam.petfam.service.like;

import com.petfam.petfam.dto.like.CommentLikeResponseDto;
import com.petfam.petfam.dto.like.PostLikeResponseDto;
import com.petfam.petfam.dto.like.ReCommentLikeResponseDto;
import com.petfam.petfam.entity.User;

public interface LikeService {

  PostLikeResponseDto likePost(Long postId, User user);

  CommentLikeResponseDto likeComment(Long commentId, User user);

  ReCommentLikeResponseDto likeReComment(Long reCommentId, User user);

}
