package com.petfam.petfam.repository;

import java.util.List;

import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReCommentRepository extends JpaRepository<ReComment, Long> {


    List<ReComment> findAllByComment(Comment comment);
}
