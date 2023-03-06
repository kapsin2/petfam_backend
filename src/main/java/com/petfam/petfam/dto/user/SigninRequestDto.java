package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = SigninRequestDto.SigninRequestDtoBuilder.class)
public class SigninRequestDto {

  private String username;
  private String password;

  public static class SigninRequestDtoBuilder {
    @JsonSetter("username")
    private String username;
    @JsonSetter("password")
    private String password;
    public SigninRequestDto build() {
      return new SigninRequestDto(username,password);
    }
  }

}
