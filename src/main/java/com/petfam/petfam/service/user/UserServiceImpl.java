package com.petfam.petfam.service.user;


import com.petfam.petfam.dto.user.AdminSigninRequestDto;
import com.petfam.petfam.dto.user.AdminSignupRequestDto;
import com.petfam.petfam.dto.user.ProfileResponseDto;
import com.petfam.petfam.dto.user.ProfileUpdateDto;
import com.petfam.petfam.dto.user.SigninRequestDto;
import com.petfam.petfam.dto.user.UserNicknameDto;
import com.petfam.petfam.dto.user.UserSignupRequestDto;
import com.petfam.petfam.dto.user.UserUsernameDto;
import com.petfam.petfam.entity.RefreshToken;
import com.petfam.petfam.entity.SignoutAccessToken;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.jwt.JwtUtil;
import com.petfam.petfam.redis.CacheKey;
import com.petfam.petfam.repository.RefreshTokenRedisRepository;
import com.petfam.petfam.repository.SignoutAccessTokenRedisRepository;
import com.petfam.petfam.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;


  // 유저 회원가입
  @Override
  @Transactional
  public String userSignup(UserSignupRequestDto usersignupRequestDto) {

    String password = passwordEncoder.encode(usersignupRequestDto.getPassword());

    _ck_username(usersignupRequestDto.getUsername());
    _ck_nickname(usersignupRequestDto.getNickname());

    User user = new User(usersignupRequestDto.getUsername(), password,
        usersignupRequestDto.getNickname(), "image",
        UserRoleEnum.USER);

    userRepository.save(user);

    return "success";
  }


  // 관리자 회원가입
  @Override
  @Transactional
  public String adminSignup(AdminSignupRequestDto adminsignupRequestDto) {
    String password = passwordEncoder.encode(adminsignupRequestDto.getPassword());

    _ck_username(adminsignupRequestDto.getUsername());
    _ck_nickname(adminsignupRequestDto.getNickname());

    if (adminsignupRequestDto.getAdminKey().equals(ADMIN_TOKEN)) {
      User admin = new User(adminsignupRequestDto.getUsername(), password,
          adminsignupRequestDto.getNickname(), "image",
          UserRoleEnum.ADMIN);

      userRepository.save(admin);

      return "success";
    } else {
      throw new IllegalArgumentException("관리자 암호가 일치하지 않습니다");
    }
  }

  // 유저 로그인
  @Override
  @Transactional
  public String signin(SigninRequestDto signinRequestDto, HttpServletResponse response) {

    User user = userRepository.findByUsername(signinRequestDto.getUsername()).orElseThrow(
        () -> new IllegalArgumentException("아이디와 비밀번호를 확인해주세요")
    );
    if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("아이디와 비밀번호를 확인해주세요");
    }

    String accessToken = jwtUtil.createToken(user.getUsername(),user.getUserRole());
    String refreshToken = jwtUtil.refreshToken(user.getUsername(),user.getUserRole());

    RefreshToken refreshToken1 = new RefreshToken(user.getUsername(),refreshToken.substring(8),jwtUtil.getRefreshTokenTime());
    refreshTokenRedisRepository.save(refreshToken1);

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER,accessToken);
    response.addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER,refreshToken);

    return "success";
  }

  // 관리자 로그인
  @Override
  @Transactional
  public String adminSignin(AdminSigninRequestDto adminSigninRequestDto,
      HttpServletResponse response) {
    User admin = userRepository.findByUsername(adminSigninRequestDto.getUsername()).orElseThrow(
        () -> new IllegalArgumentException("아이디와 비밀번호를 확인해주세요")
    );
    if (!passwordEncoder.matches(adminSigninRequestDto.getPassword(), admin.getPassword())) {
      throw new IllegalArgumentException("아이디와 비밀번호를 확인해주세요");
    }

    if (admin.getUserRole() != UserRoleEnum.ADMIN) {
      throw new IllegalArgumentException("관리자 계정이아닙니다.");
    }

    if (admin.getUserRole() == UserRoleEnum.ADMIN) {
      if (!adminSigninRequestDto.getAdminKey().equals(ADMIN_TOKEN)) {
        throw new IllegalArgumentException("관리자 암호를 확인해주세요");
      }
    }

    String accessToken = jwtUtil.createToken(admin.getUsername(),admin.getUserRole());
    String refreshToken = jwtUtil.refreshToken(admin.getUsername(),admin.getUserRole());

    RefreshToken refreshToken1 = new RefreshToken(admin.getUsername(),refreshToken.substring(8),jwtUtil.getRefreshTokenTime());
    refreshTokenRedisRepository.save(refreshToken1);

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER,accessToken);
    response.addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER,refreshToken);


    return "success";
  }

  // 로그아웃
  @Override
  @Transactional
  @CacheEvict(value = CacheKey.USER, key = "#username")
  public String signout(HttpServletRequest request,String username) {
    String accessToken = jwtUtil.resolveToken(request);
    long remainMilliSeconds = jwtUtil.getRemainMilliSeconds(accessToken);
    refreshTokenRedisRepository.deleteById(username);
    signoutAccessTokenRedisRepository.save(SignoutAccessToken.of(accessToken,username, remainMilliSeconds));

    return "success";
  } //추후 구현


  // 프로필 업데이트
  @Override
  @Transactional
  public String updateProfile(ProfileUpdateDto profileUpdateDto, User user) {
    user.updateProfile(profileUpdateDto);
    userRepository.save(user);
    return "success";
  }

  // 프로필 가져오기
  @Override
  @Transactional
  public ProfileResponseDto getProfile(Long userId) {
    User user = _findUser(userId);
    return ProfileResponseDto.builder().nickname(user.getNickname()).id(user.getId()).image(user.getImage()).introduction(user.getIntroduction()).role(user.getUserRole().getAuthority()).build();
  }

  // 토큰 리프레쉬
  @Override
  @Transactional
  public String refresh(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = jwtUtil.resolveRefreshToken(request); //리프레시토큰

    Claims refreshInfo = jwtUtil.getUserInfoFromToken(refreshToken);

    if (!refreshInfo.getSubject().isEmpty()) {
      User user = _findUser(refreshInfo.getSubject());
      response.addHeader(JwtUtil.AUTHORIZATION_HEADER,
          jwtUtil.createToken(user.getUsername(), user.getUserRole()));
      return "success";
    } else {
      throw new IllegalArgumentException("다시 로그인 해주세요");
    }
  }
  public String ck_username(UserUsernameDto userUsernameDto) {
    if (userRepository.findByUsername(userUsernameDto.getUsername()).isEmpty()) {
      return "success";
    } else return "fail";
  }

  public String ck_nickname(UserNicknameDto userNicknameDto) {
    if (userRepository.findByNickname(userNicknameDto.getNickname()).isEmpty()) {
      return "success";
    } else return "fail";
  }
  private void _ck_username(String username) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 유저입니다.");
    }
  }

  private void _ck_nickname(String nickname) {
    if (userRepository.findByNickname(nickname).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }

  private User _findUser(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("유저 정보가 존재하지 않습니다.")
    );
    return user;
  }

  private User _findUser(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new IllegalArgumentException("유저 정보가 존재하지 않습니다.")
    );
    return user;
  }
}
