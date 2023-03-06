package com.petfam.petfam.repository;

import com.petfam.petfam.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findAllByTalkId(Long talkId);
}
