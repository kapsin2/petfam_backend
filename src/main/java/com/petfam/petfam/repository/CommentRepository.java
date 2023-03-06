package com.petfam.petfam.repository;

import java.util.List;

import com.petfam.petfam.entity.Comment;
import com.petfam.petfam.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByPost(Post post);
}
