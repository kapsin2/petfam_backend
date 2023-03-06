package com.petfam.petfam.dto.user;

import lombok.Getter;

@Getter
public class KakaoUserInfoDto {
  private Long id;
  private String nickname;

  public KakaoUserInfoDto(Long id, String nickname) {
    this.id = id;
    this.nickname = nickname;
  }
}
