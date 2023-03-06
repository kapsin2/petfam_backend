package com.petfam.petfam.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfam.petfam.dto.user.KakaoUserInfoDto;
import com.petfam.petfam.entity.RefreshToken;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.jwt.JwtUtil;
import com.petfam.petfam.repository.RefreshTokenRedisRepository;
import com.petfam.petfam.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  public String kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
    //1. 인가 코드로 엑세스 토큰요청
    String accessToken = getToken(code);

    //2.토큰으로 카카오 API 호출 : 엑세스토큰으로 카카오 사용자 정보 가져오기
    KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

    // 3. 필요시에 회원가입
    User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

    // 4. JWT 토큰 반환
    String accessToken1 =  jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getUserRole());
    String refreshToken = jwtUtil.refreshToken(kakaoUser.getUsername(), kakaoUser.getUserRole());

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken1);
    response.addHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER, refreshToken);

    RefreshToken refreshToken1 = new RefreshToken(kakaoUser.getUsername(),refreshToken.substring(8),jwtUtil.getRefreshTokenTime());
    refreshTokenRedisRepository.save(refreshToken1);



    return "success";
  }
  private String getToken(String code) throws JsonProcessingException {
    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP Body 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", "d4a2b1bc9deb142df4a22bfdd73d9727");
    body.add("redirect_uri", "http://localhost:8080/users/kakao/callback");
    body.add("code", code);

    // HTTP 요청 보내기
    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
        new HttpEntity<>(body, headers);
    RestTemplate rt = new RestTemplate();
    ResponseEntity<String> response = rt.exchange(
        "https://kauth.kakao.com/oauth/token",
        HttpMethod.POST,
        kakaoTokenRequest,
        String.class
    );

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    return jsonNode.get("access_token").asText();
  }
  private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP 요청 보내기
    HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
    RestTemplate rt = new RestTemplate();
    ResponseEntity<String> response = rt.exchange(
        "https://kapi.kakao.com/v2/user/me",
        HttpMethod.POST,
        kakaoUserInfoRequest,
        String.class
    );

    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    Long id = jsonNode.get("id").asLong();
    String nickname = jsonNode.get("properties")
        .get("nickname").asText();

    log.info("카카오 사용자 정보: " + id + ", " + nickname);
    return new KakaoUserInfoDto(id, nickname);
  }

  private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
    // DB 에 중복된 Kakao Id 가 있는지 확인
    Long kakaoId = kakaoUserInfo.getId();
    User kakaoUser = userRepository.findByKakaoId(kakaoId)
        .orElse(null);
    if (kakaoUser == null) {
      // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인

        // 신규 회원가입
        // password: random UUID
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);


        kakaoUser = new User(kakaoUserInfo.getNickname(), kakaoId, encodedPassword,kakaoUserInfo.getNickname());

      userRepository.save(kakaoUser);
    }
    return kakaoUser;
  }

}
