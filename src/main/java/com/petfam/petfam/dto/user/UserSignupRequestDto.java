package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UserSignupRequestDto.UserSignupRequestDtoBuilder.class)
public class UserSignupRequestDto {

  private String username;
  private String password;
  private String nickname;

  public static class UserSignupRequestDtoBuilder {
    @JsonSetter("username")
    private String username;
    @JsonSetter("password")
    private String password;
    @JsonSetter("nickname")
    private String nickname;

    public UserSignupRequestDto build() {
      return new UserSignupRequestDto(username,password,nickname);
    }
  }

}
