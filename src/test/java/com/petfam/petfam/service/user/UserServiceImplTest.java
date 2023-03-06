package com.petfam.petfam.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petfam.petfam.dto.user.AdminSigninRequestDto;
import com.petfam.petfam.dto.user.AdminSignupRequestDto;
import com.petfam.petfam.dto.user.ProfileResponseDto;
import com.petfam.petfam.dto.user.ProfileUpdateDto;
import com.petfam.petfam.dto.user.SigninRequestDto;
import com.petfam.petfam.dto.user.UserSignupRequestDto;
import com.petfam.petfam.entity.SignoutAccessToken;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.jwt.JwtUtil;
import com.petfam.petfam.repository.RefreshTokenRedisRepository;
import com.petfam.petfam.repository.SignoutAccessTokenRedisRepository;
import com.petfam.petfam.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;


class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtUtil jwtUtil;
  @Mock
  private RefreshTokenRedisRepository refreshTokenRedisRepository;
  @Mock
  private SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;


  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("유저회원가입")
  void userSignup() {
    //giver
    UserSignupRequestDto requestDto = UserSignupRequestDto.builder().username("user").password("123").nickname("kap").build();
    when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");
    when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(requestDto.getNickname())).thenReturn(Optional.empty());

    //when
    String result = userService.userSignup(requestDto);

    //then
    assertEquals("success",result);
    verify(userRepository).save(argThat(user -> {
      return user.getUsername().equals("user") &&
          user.getPassword().equals("password") &&
          user.getNickname().equals("kap") &&
          user.getUserRole().equals(UserRoleEnum.USER);
    }));
  }

  @Test
  @DisplayName("유저회원가입-중복아이디")
  void userSignup_ck_username() {
    //giver
    UserSignupRequestDto requestDto = UserSignupRequestDto.builder().username("user").password("123").nickname("kap").build();
    User user = new User(requestDto.getUsername(), "password",
        requestDto.getNickname(), "image",
        UserRoleEnum.USER);
    when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");
    when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.userSignup(requestDto);
    });

  }

  @Test
  @DisplayName("유저회원가입-중복닉네임")
  void userSignup_ck_nickname() {
    //giver
    UserSignupRequestDto requestDto = UserSignupRequestDto.builder().username("user").password("123").nickname("kap").build();
    User user = new User(requestDto.getUsername(), "password",
        requestDto.getNickname(), "image",
        UserRoleEnum.USER);
    when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");
    when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(requestDto.getNickname())).thenReturn(Optional.of(user));

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.userSignup(requestDto);
    });

  }

  @Test
  @DisplayName("관리자회원가입")
  void adminSignup() {
    //giver
    AdminSignupRequestDto requestDto = AdminSignupRequestDto.builder().username("admin").password("123").nickname("admin").adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC").build();
    when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");
    when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(requestDto.getNickname())).thenReturn(Optional.empty());

    //when
    String result = userService.adminSignup(requestDto);
    //then
    assertEquals("success",result);
    verify(userRepository).save(argThat(user -> {
      return user.getUsername().equals("admin") &&
            user.getPassword().equals("password") &&
            user.getNickname().equals("admin") &&
            user.getUserRole().equals(UserRoleEnum.ADMIN);
    }));
  }

  @Test
  @DisplayName("관리자회원가입-관리자암호 오류")
  void adminSignup_adminKey() {
    //giver
    AdminSignupRequestDto requestDto = AdminSignupRequestDto.builder().username("admin").password("123").nickname("admin").adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygo").build();
    when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("password");
    when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByNickname(requestDto.getNickname())).thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.adminSignup(requestDto);
    });
  }

  @Test
  @DisplayName("유저로그인")
  void signin() {
    // giver
    SigninRequestDto signinRequestDto = SigninRequestDto.builder().username("user").password("password").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    User user = new User("user", passwordEncoder.encode("password"), "nickname", "image", UserRoleEnum.USER);
    when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
    when(jwtUtil.createToken("user", UserRoleEnum.USER)).thenReturn("accessToken");
    when(jwtUtil.refreshToken("user", UserRoleEnum.USER)).thenReturn("refreshToken");
    when(jwtUtil.getRefreshTokenTime()).thenReturn(1000L);

    // when
    String result = userService.signin(signinRequestDto, response);

    // then
    assertEquals("success", result);
    verify(response).addHeader(JwtUtil.AUTHORIZATION_HEADER, "accessToken");
    verify(response).addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER, "refreshToken");
    verify(refreshTokenRedisRepository).save(argThat(refreshToken1 -> {
      return refreshToken1.getRefreshToken().equals("oken") &&
          refreshToken1.getId().equals("user") &&
          refreshToken1.getExpiration().equals(1000L);
    }));
  }

  @Test
  @DisplayName("유저로그인-아이디오류")
  void signin_ck_username() {
    // giver
    SigninRequestDto signinRequestDto = SigninRequestDto.builder().username("user1").password("password").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.signin(signinRequestDto, response);
    });


  }

  @Test
  @DisplayName("유저로그인-비밀번호오류")
  void signin_ck_password() {
    // giver
    SigninRequestDto signinRequestDto = SigninRequestDto.builder().username("user").password("password").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    User user = new User("user", passwordEncoder.encode("password"), "nickname", "image", UserRoleEnum.USER);
    when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.signin(signinRequestDto, response);
    });
  }

  @Test
  @DisplayName("관리자로그인")
  void adminSignin() {
    //giver
    AdminSigninRequestDto requestDto = AdminSigninRequestDto.builder().username("admin").password("123").adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    User user = new User("admin",passwordEncoder.encode("123"),"admin","image",UserRoleEnum.ADMIN);
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("123",user.getPassword())).thenReturn(true);
    when(jwtUtil.createToken("admin",UserRoleEnum.ADMIN)).thenReturn("accessToken");
    when(jwtUtil.refreshToken("admin",UserRoleEnum.ADMIN)).thenReturn("refreshToken");
    when(jwtUtil.getRefreshTokenTime()).thenReturn(1000L);

    //when
    String result = userService.adminSignin(requestDto,response);

    //then
    assertEquals("success", result);
    verify(response).addHeader(JwtUtil.AUTHORIZATION_HEADER, "accessToken");
    verify(response).addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER, "refreshToken");
  verify(refreshTokenRedisRepository).save(argThat(refreshToken1 -> {
    return refreshToken1.getRefreshToken().equals("oken") &&
        refreshToken1.getId().equals("admin") &&
        refreshToken1.getExpiration().equals(1000L);
  }));
  }

  @Test
  @DisplayName("관리자로그인-관리자계정X")
  void adminSignin_ck_role() {
    //giver
    AdminSigninRequestDto requestDto = AdminSigninRequestDto.builder().username("admin").password("123").adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    User user = new User("admin",passwordEncoder.encode("123"),"admin","image",UserRoleEnum.USER);
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("123",user.getPassword())).thenReturn(true);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.adminSignin(requestDto,response);
    });

  }

  @Test
  @DisplayName("관리자로그인-관리자암호오류")
  void adminSignin_ck_adminKey() {
    //giver
    AdminSigninRequestDto requestDto = AdminSigninRequestDto.builder().username("admin").password("123").adminKey("AAABnvxRVklrnYxKZ0aHgTBcXukeZygo").build();
    HttpServletResponse response = mock(HttpServletResponse.class);
    User user = new User("admin",passwordEncoder.encode("123"),"admin","image",UserRoleEnum.ADMIN);
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("123",user.getPassword())).thenReturn(true);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.adminSignin(requestDto,response);
    });
  }

  @Test
  @DisplayName("로그아웃")
  void signout() {
    //giver
    HttpServletRequest request = mock(HttpServletRequest.class);
    String accessToken = "accessToken";
    String username = "user";
    Long time = 1L;
    when(jwtUtil.resolveToken(request)).thenReturn(accessToken);
    when(jwtUtil.getRemainMilliSeconds(any(String.class))).thenReturn(time);

    //when
    String result = userService.signout(request,username);

    //then
    assertEquals("success",result);
    verify(refreshTokenRedisRepository).deleteById(username);
    verify(signoutAccessTokenRedisRepository).save(any(SignoutAccessToken.class));
  }

  @Test
  @DisplayName("프로필업데이트")
  void updateProfile() {
    //giver
    ProfileUpdateDto request = ProfileUpdateDto.builder().nickname("kap11").introduction("안녕하세요").image("image11").build();
    User user = new User("user","123","kap","image", UserRoleEnum.USER);

    //when
    String result = userService.updateProfile(request,user);

    //then
    assertEquals("success",result);
    verify(userRepository).save(argThat(user1 -> {
      return user1.getNickname().equals("kap11") &&
          user1.getIntroduction().equals("안녕하세요") &&
          user1.getImage().equals("image11");
    }));
  }

  @Test
  @DisplayName("프로필일부 업데이트")
  void updateProfile_part() {
    //giver
    ProfileUpdateDto request = ProfileUpdateDto.builder().nickname("").introduction("안녕하세요").image("image11").build();
    User user = new User("user","123","kap","image", UserRoleEnum.USER);

    //when
    String result = userService.updateProfile(request,user);

    //then
    assertEquals("success",result);
    verify(userRepository).save(argThat(user1 -> {
      return user1.getNickname().equals("kap") &&
          user1.getIntroduction().equals("안녕하세요") &&
          user1.getImage().equals("image11");
    }));
  }

  @Test
  @DisplayName("프로필가져오기")
  void getProfile() {
    //giver
    Long userId = 1L;
    User user = new User("user","123","kap","image", UserRoleEnum.USER);
    user.setId(userId);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    //when
    ProfileResponseDto result = userService.getProfile(user.getId());
    //then
    ProfileResponseDto responseDto = ProfileResponseDto.builder().id(user.getId()).introduction(user.getIntroduction()).image(user.getImage()).nickname(user.getNickname()).role(user.getUserRole().getAuthority()).build();
    assertEquals(responseDto.getNickname(),result.getNickname());
    assertEquals(responseDto.getImage(),result.getImage());
    assertEquals(responseDto.getIntroduction(),result.getIntroduction());
    assertEquals(responseDto.getRole(),result.getRole());
  }

  @Test
  @DisplayName("프로필가져오기_유저X")
  void getProfile_ck_username() {
    //giver
    Long userId = 1L;
    User user = new User("user","123","kap","image", UserRoleEnum.USER);
    user.setId(userId);
    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.getProfile(user.getId());
    });
  }

  @Test
  @DisplayName("리프레시")
  void refresh() {
    //giver
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    String refreshToken = "refreshToken";
    User user = new User("user","123","kap","image",UserRoleEnum.USER);
    Claims refreshinfo = mock(Claims.class);

    when(jwtUtil.resolveRefreshToken(request)).thenReturn(refreshToken);
    when(jwtUtil.getUserInfoFromToken(refreshToken)).thenReturn(refreshinfo);
    when(refreshinfo.getSubject()).thenReturn(user.getUsername());
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    when(jwtUtil.createToken(user.getUsername(),user.getUserRole())).thenReturn("accessToken");

    //when
    String result = userService.refresh(request,response);

    //then
    assertEquals("success",result);
    verify(response).addHeader(JwtUtil.AUTHORIZATION_HEADER,"accessToken");
  }

  @Test
  @DisplayName("리프레시_유효하지 않은 리프레시토큰")
  void refresh_ck_refreshToken() {
    //giver
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    String refreshToken = "refreshToken";
    Claims refreshinfo = mock(Claims.class);

    when(jwtUtil.resolveRefreshToken(request)).thenReturn(refreshToken);
    when(jwtUtil.getUserInfoFromToken(refreshToken)).thenReturn(refreshinfo);
    when(refreshinfo.getSubject()).thenReturn(String.valueOf(Optional.empty()));

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      userService.refresh(request,response);
    });
  }
}