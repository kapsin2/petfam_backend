package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = AdminSigninRequestDto.AdminSigninRequestDtoBuilder.class)
public class AdminSigninRequestDto {

  private String username;

  private String password;

  private String adminKey;

  public static class AdminSigninRequestDtoBuilder {
    @JsonSetter("username")
    private String username;
    @JsonSetter("password")
    private String password;
    @JsonSetter("adminKey")
    private String adminKey;
    public AdminSigninRequestDto build() {
      return new AdminSigninRequestDto(username,password,adminKey);
    }
  }
}
