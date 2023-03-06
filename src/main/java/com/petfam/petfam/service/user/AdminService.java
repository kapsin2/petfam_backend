package com.petfam.petfam.service.user;

import com.petfam.petfam.dto.user.UserResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

  // List<UserResponseDto> getUsers(Pageable pageable)
  Page<UserResponseDto> getUsers(Pageable pageable);
}
