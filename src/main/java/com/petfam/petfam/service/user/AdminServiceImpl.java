package com.petfam.petfam.service.user;


import com.petfam.petfam.dto.user.UserResponseDto;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

@Override
@Transactional(readOnly = true)
public Page<UserResponseDto> getUsers(Pageable pageable) {

  Page<User> users = userRepository.findAll(pageable);
  List<UserResponseDto> userResponseDtoList = new ArrayList<>();

  for (User user : users) {
    UserResponseDto userResponseDto = UserResponseDto.builder().username(user.getUsername()).id(user.getId()).nickname(
        user.getNickname()).role(user.getUserRole().getAuthority()).build();
    userResponseDtoList.add(userResponseDto);
  }
  return new PageImpl<>(userResponseDtoList,pageable,users.getTotalElements());
}

}
