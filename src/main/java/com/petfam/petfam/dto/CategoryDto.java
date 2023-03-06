package com.petfam.petfam.dto;

import com.petfam.petfam.entity.enums.CategoryEnum;
import lombok.Getter;

@Getter
public class CategoryDto {

  private CategoryEnum id;
  private String name;

  public CategoryDto(CategoryEnum category) {
    this.id = category;
    this.name = category.getName();
  }
}
