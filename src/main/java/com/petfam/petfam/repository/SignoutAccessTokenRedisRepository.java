package com.petfam.petfam.repository;

import com.petfam.petfam.entity.SignoutAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignoutAccessTokenRedisRepository extends CrudRepository<SignoutAccessToken, String> {
}
