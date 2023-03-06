package com.petfam.petfam.dto;

import lombok.Getter;

@Getter
public class SecurityExceptionDto {
  private String msg;
  private int statusCode;
  public SecurityExceptionDto(int statusCode, String msg) {
    this.msg = msg;
    this.statusCode = statusCode;
  }
}
