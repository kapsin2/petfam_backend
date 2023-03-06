package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UserUsernameDto.UserUsernameDtoBuilder.class)
public class UserUsernameDto {
  private String username;

  public static class UserUsernameDtoBuilder{
    @JsonSetter("username")
    private String username;

    public UserUsernameDto build() {
      return new UserUsernameDto(username);
    }
  }
}
