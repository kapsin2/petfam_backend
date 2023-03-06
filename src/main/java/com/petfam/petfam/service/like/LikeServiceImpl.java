package com.petfam.petfam.service.like;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final PostRepository postRepository;

  private final LikeRepository likeRepository;

  private final CommentRepository commentRepository;

  private final ReCommentRepository reCommentRepository;

  @Override
  @Transactional
  public PostLikeResponseDto likePost(Long postId, User user) {
    boolean islike = false;
    String msg = "";

    Likes likelog = likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.POST, user.getId(),
        postId).orElse(null);

    if (likelog == null) {
      likelog = new Likes(user, postId, LikeEnum.POST);
      islike = true;
      likeRepository.save(likelog);
      msg = "좋아요를 누르셨습니다.";
    } else {
      islike = false;
      likeRepository.delete(likelog);
      msg = "좋아요를 취소하셨습니다.";
    }

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

    post.updateLike(islike);

    return new PostLikeResponseDto(msg, 200);
  }

  @Override
  @Transactional
  public CommentLikeResponseDto likeComment(Long commentId, User user) {
    boolean islike = false;
    String msg = "";

    Likes likelog = likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.COMMENT, user.getId(),
        commentId).orElse(null);

    if (likelog == null) {
      likelog = new Likes(user, commentId, LikeEnum.COMMENT);
      islike = true;
      likeRepository.save(likelog);
      msg = "좋아요를 누르셨습니다.";
    } else {
      islike = false;
      likeRepository.delete(likelog);
      msg = "좋아요를 취소하셨습니다.";
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

    comment.updateLike(islike);

    return new CommentLikeResponseDto(msg, 200);
  }

  @Override
  @Transactional
  public ReCommentLikeResponseDto likeReComment(Long reCommentId, User user) {
    boolean islike = false;
    String msg = "";

    Likes likelog = likeRepository.findByTypeAndUser_IdAndTargetId(LikeEnum.RECOMMENT, user.getId(),
        reCommentId).orElse(null);

    if (likelog == null) {
      likelog = new Likes(user, reCommentId, LikeEnum.RECOMMENT);
      islike = true;
      likeRepository.save(likelog);
      msg = "좋아요를 누르셨습니다.";
    } else {
      islike = false;
      likeRepository.delete(likelog);
      msg = "좋아요를 취소하셨습니다.";
    }

    ReComment reComment = reCommentRepository.findById(reCommentId)
        .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

    reComment.updateLike(islike);

    return new ReCommentLikeResponseDto(msg, 200);
  }

}
