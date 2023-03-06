package com.petfam.petfam.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.petfam.petfam.dto.user.UserResponseDto;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class AdminServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AdminServiceImpl adminService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("전체유저조회하기")
  void testGetUsers() {

    //giver
    User user1 = new User("user1", "password1","kap1" ,"image1",UserRoleEnum.USER);
    User user2 = new User("user2", "password2","kap2" ,"image2",UserRoleEnum.USER);

    Pageable pageable = Pageable.ofSize(10).withPage(1);
    Page<User> users = new PageImpl<>(Arrays.asList(user1, user2), pageable, 2);
    when(userRepository.findAll(pageable)).thenReturn(users);

    //when
    Page<UserResponseDto> userResponseDtoPage = adminService.getUsers(pageable);

    //then
    List<UserResponseDto> userResponseDtoList = userResponseDtoPage.getContent();
    assertEquals(2, userResponseDtoList.size());

    UserResponseDto userResponseDto1 = userResponseDtoList.get(0);
    assertEquals(user1.getUsername(), userResponseDto1.getUsername());
    assertEquals(user1.getUserRole().getAuthority(), userResponseDto1.getRole());

    UserResponseDto userResponseDto2 = userResponseDtoList.get(1);
    assertEquals(user2.getUsername(), userResponseDto2.getUsername());
    assertEquals(user2.getUserRole().getAuthority(), userResponseDto2.getRole());

  }
}

