package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = ProfileUpdateDto.ProfileUpdateDtoBuilder.class)
public class ProfileUpdateDto {
  private String nickname;
  private String introduction;
  private String image;

  public static class ProfileUpdateDtoBuilder {
    @JsonSetter("nickname")
    private String nickname;
    @JsonSetter("introduction")
    private String introduction;
    @JsonSetter("image")
    private String image;
    public ProfileUpdateDto build() {
      return new ProfileUpdateDto(nickname,introduction,image);
    }
  }
}
