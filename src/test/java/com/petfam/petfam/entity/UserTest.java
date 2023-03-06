package com.petfam.petfam.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  @DisplayName("정상 케이스")
  void createUser_Normal(){
    // given
    String username = "haeri";
    String password = "pass";
    String nickname = "안해리";


    // when
    User user = User.builder().username("haeri").password("pass").nickname("안해리").build();


    // then
    assertEquals(username, user.getUsername());
    assertEquals(password, user.getPassword());
    assertEquals(nickname, user.getNickname());
    assertEquals(0, user.getPoint());
    assertEquals("src/main/java/resources/static/images/m_20220509173224_d9N4ZGtBVR.jpeg", user.getImage());
    assertEquals("안녕하세요.",user.getIntroduction());
  }

}