package com.petfam.petfam.dto.user;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = ProfileResponseDto.ProfileResponseDtoBuilder.class)
public class ProfileResponseDto {

  private Long id;
  private String nickname;
  private String introduction;
  private String image;
  private String role;

  public static class ProfileResponseDtoBulder {
    @JsonSetter("id")
    private Long id;
    @JsonSetter("nickname")
    private String nickname;
    @JsonSetter("introduction")
    private String introduction;
    @JsonSetter("image")
    private String image;
    @JsonSetter("role")
    private String role;

    public ProfileResponseDto build() {
      return new ProfileResponseDto(id,nickname,introduction,image,role);
    }
  }
}
