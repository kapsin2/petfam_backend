package com.petfam.petfam.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfam.petfam.dto.user.AdminSigninRequestDto;
import com.petfam.petfam.dto.user.AdminSignupRequestDto;
import com.petfam.petfam.dto.user.SigninRequestDto;
import com.petfam.petfam.dto.user.UserSignupRequestDto;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;
  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @DisplayName("유저회원가입")
  void userSignup() throws Exception {
    // giver
    UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
        .username("user")
        .password("123")
        .nickname("kap")
        .build();
    String requestBody = new ObjectMapper().writeValueAsString(requestDto);
    when(userService.userSignup(any())).thenReturn("success");

    // when
    MvcResult result = mockMvc.perform(post("/users/signup")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andReturn();
    System.out.println(result.getResponse().getContentAsString()+"-------------------------------------------------------------");
    // then
    assertEquals("success",result.getResponse().getContentAsString());
  }



  @Test
  @DisplayName("관리자회원가입")
  void adminSignup() throws Exception {
    //giver
    AdminSignupRequestDto requestDto = AdminSignupRequestDto.builder()
        .username("admin")
        .password("123")
        .nickname("admin")
        .adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC")
        .build();
    String requestBody = new ObjectMapper().writeValueAsString(requestDto);
    when(userService.adminSignup(any(AdminSignupRequestDto.class))).thenReturn("success");
    //when
    MvcResult result =mockMvc.perform(post("/users/admin/signup")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(requestBody))
        .andExpect(status().isOk())
        .andReturn();
    //then
    assertEquals("success",result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("유저로그인")
  void signin() throws Exception{
    //giver
    SigninRequestDto requestDto = SigninRequestDto.builder()
        .username("user")
        .password("123")
        .build();
    String requestBody = new ObjectMapper().writeValueAsString(requestDto);
    when(userService.signin(any(SigninRequestDto.class),any(HttpServletResponse.class))).thenReturn("success");
    //when
    MvcResult result = mockMvc.perform(post("/users/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andReturn();
    //then
    assertEquals("success",result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("관리자로그인")
  void adminSignin() throws Exception{
    //giver
    AdminSigninRequestDto requestDto = AdminSigninRequestDto.builder()
        .username("admin")
        .password("123")
        .adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC")
        .build();
    String requestBody = new ObjectMapper().writeValueAsString(requestDto);
    when(userService.adminSignin(any(AdminSigninRequestDto.class),any(HttpServletResponse.class))).thenReturn("success");
    //when
    MvcResult result = mockMvc.perform(post("/users/admin/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andReturn();
    //then
    assertEquals("success",result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("로그아웃")
  void signout() throws Exception {
    // given
    User user = new User("user", "password",
        "kap", "image",
        UserRoleEnum.USER);
    UserDetailsImpl userDetails = new UserDetailsImpl(user,"user");
    when(userService.signout(any(HttpServletRequest.class), any(String.class))).thenReturn("success");

    // when
    MvcResult result = mockMvc.perform(post("/users/signout")
            .param("username", userDetails.getUsername()))
        .andExpect(status().isOk())
        .andReturn();

    // then
    assertEquals("success",result.getResponse().getContentAsString());
  }



//  @Test
//  @DisplayName("프로필업데이트")
//  void updateProfile() throws Exception{
//    // giver
//    User user = new User("user", "password",
//        "kap", "image",
//        UserRoleEnum.USER);
//    UserDetailsImpl userDetails = new UserDetailsImpl(user,user.getUsername());
//    ProfileUpdateDto updateDto = ProfileUpdateDto.builder()
//        .nickname("kap11")
//        .image("image2")
//        .introduction("반갑습니다.")
//        .build();
//    String requestBody = new ObjectMapper().writeValueAsString(updateDto);
//
//    when(userService.updateProfile(any(ProfileUpdateDto.class), any(User.class))).thenReturn("success");
//
//    // when
//    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/profiles")
//            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(requestBody))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    // then
//    assertEquals("success", result.getResponse().getContentAsString());
//  }
//
//
//  @Test
//  @DisplayName("프로필 가져오기")
//  void getProfile() throws Exception {
//    // given
//    User user = new User("user", "password",
//        "kap", "image",
//        UserRoleEnum.USER);
//    UserDetailsImpl userDetails = new UserDetailsImpl(user,user.getUsername());
//    ProfileResponseDto responseDto = ProfileResponseDto.builder()
//        .id(1L)
//        .nickname("kap")
//        .introduction("안녕하세요")
//        .image("image")
//        .role("ROLE_USER")
//        .build();
//    when(userService.getProfile(user.getId())).thenReturn(responseDto);
//
//    // when
//    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/profiles")
//            .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    // then
//    ObjectMapper objectMapper = new ObjectMapper();
//    ProfileResponseDto actualDto = objectMapper.readValue(
//        result.getResponse().getContentAsString(), ProfileResponseDto.class);
//    assertEquals(responseDto, actualDto);
//  }

  @Test
  @DisplayName("리프레시 토큰")
  void refresh() throws Exception{
    //giver
    when(userService.refresh(any(HttpServletRequest.class),any(HttpServletResponse.class))).thenReturn("success");
    //when
    MvcResult result =mockMvc.perform(post("/users/refresh"))
        .andExpect(status().isOk())
        .andReturn();
    //then
    assertEquals("success",result.getResponse().getContentAsString());
  }
}