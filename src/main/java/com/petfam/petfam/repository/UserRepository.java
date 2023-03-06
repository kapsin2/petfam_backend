package com.petfam.petfam.repository;

import com.petfam.petfam.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
  Optional<User> findByKakaoId(Long id);
  Optional<User> findByNickname(String nickname);

  Page<User> findAll(Pageable pageable);

}
