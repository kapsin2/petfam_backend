package com.petfam.petfam.jwt;

import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final UserDetailsServiceImpl userDetailsService;


  //토큰 생성에 필요한 값
  public static final String AUTHORIZATION_HEADER = "Authorization"; //Header KEY 값
  public static final String REFRESH_AUTHORIZATION_HEADER = "Refresh_authorization"; //Header KEY 값
  public static final String AUTHORIZATION_KEY = "auth";  // 사용자 권한 값의 KEY.
  public static final String BEARER_PREFIX = "Bearer "; //토큰 식별자.
  public static final String REFRESH_PREFIX = "Refresh "; //토큰 식별자.
  private static final Long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
  private static final Long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14 day
  @Value("${jwt.secret.key}")       //properties의 값을 읽어온다.
  private String secretKey;         //비밀키 jwt암호 해독의 필수키
  private Key key;                   //secretKey를 사용할수있게 변화시킨 key
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct //의존성주입이 이루어진후 초기화 하는 매서드 로직이 실행되기전에 수행된다.
  public void init() {
    byte[] bytes = Base64.getDecoder()
        .decode(secretKey);  //secretKey는 64진법으로 이루어져서 이것을 해독해서 byte코드로 만들고
    key = Keys.hmacShaKeyFor(bytes);                       //Key로 변환시켜 key로 넣어준다
  }

  public Long getRefreshTokenTime() {
    return REFRESH_TOKEN_TIME;
  }


  // 토큰 생성
  public String createToken(String username, UserRoleEnum role) {
    Date date = new Date();
    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  // 리프레시 토큰 생성
  public String refreshToken(String username, UserRoleEnum role) {
    Date date = new Date();
    return REFRESH_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  //클라이언트 요청에서 토큰을 찾아내고 BEARER_PREFIX를 제거하여 key로 해독가능한 상태로 만들어준다.
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(
        AUTHORIZATION_HEADER);  //HttpServletRequest매서드 .getHeader로 request에서 토큰을 찾는다.
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(
        BEARER_PREFIX)) {  //찾은 토큰이 text를 가지고있고 시작부분이 위에 정한 BEARER_PREFIX 와 같다면
      return bearerToken.substring(7); //"Bearer " 를 지워주기위해 substring을 사용한다.
    }
    return null;
  }

  public String resolveRefreshToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(REFRESH_AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(REFRESH_PREFIX)) {
      return bearerToken.substring(8);
    }
    return null;
  }

  // 토큰 검증
  public boolean validateToken(String token) {

    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      throw new MalformedJwtException("Invalid JWT signature");
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "Expired JWT token");
    } catch (UnsupportedJwtException e) {
      throw new UnsupportedJwtException("Unsupported JWT token");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("JWT claims is empty");
    }
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // 인증 객체 생성
  public Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  //남은 시간 계산
  public long getRemainMilliSeconds(String token) {
    Claims info = getUserInfoFromToken(token);
    Date expiration = info.getExpiration();
    Date now = new Date();
    return expiration.getTime() - now.getTime();
  }


}