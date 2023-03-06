package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UserNicknameDto.UserNicknameDtoBuilder.class)
public class UserNicknameDto {
  private String nickname;

  public static class UserNicknameDtoBuilder{
    @JsonSetter("nickname")
    private String nickname;
    public UserNicknameDto build() {
      return new UserNicknameDto(nickname);
    }
  }
}
