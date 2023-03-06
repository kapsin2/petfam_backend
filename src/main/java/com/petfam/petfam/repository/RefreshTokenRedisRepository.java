package com.petfam.petfam.repository;

import com.petfam.petfam.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findByRefreshToken(String token);
}
