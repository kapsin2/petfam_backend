package com.petfam.petfam.repository;

import com.petfam.petfam.entity.Talk;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

  Optional<Talk> findByApplyIdAndReceiveId(Long applyId,Long receivedId);

}
