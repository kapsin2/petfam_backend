package com.petfam.petfam.repository;

import com.petfam.petfam.entity.Likes;
import com.petfam.petfam.entity.enums.LikeEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findByTypeAndUser_IdAndTargetId(LikeEnum type, Long userId, Long targetId);
}
