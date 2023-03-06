package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UserResponseDto.UserResponseDtoBuilder.class)
public class UserResponseDto {

  private Long id;
  private String username;
  private String nickname;
  private String role;

  public static class UserResponseDtoBuilder {
    @JsonSetter("id")
    private Long id;
    @JsonSetter("username")
    private String username;
    @JsonSetter("nickname")
    private String nickname;
    @JsonSetter("role")
    private String role;
    public UserResponseDto build(){
      return new UserResponseDto(id,username,nickname,role);
    }
  }


}
