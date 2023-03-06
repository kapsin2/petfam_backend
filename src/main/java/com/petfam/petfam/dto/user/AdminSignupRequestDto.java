package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = AdminSignupRequestDto.AdminSignupRequestDtoBuilder.class)
public class AdminSignupRequestDto {

  private String username;
  private String password;
  private String nickname;
  private String adminKey;

  public static class AdminSignupRequestDtoBuilder{
    @JsonSetter("username")
    private String username;
    @JsonSetter("password")
    private String password;
    @JsonSetter("nickname")
    private String nickname;
    @JsonSetter("adminKey")
    private String adminKey;

    public AdminSignupRequestDto build() {
      return new AdminSignupRequestDto(username,password,nickname,adminKey);
    }
  }
}
