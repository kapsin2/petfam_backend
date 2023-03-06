package com.petfam.petfam.jwt;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfam.petfam.dto.SecurityExceptionDto;

import com.petfam.petfam.repository.RefreshTokenRedisRepository;
import com.petfam.petfam.repository.SignoutAccessTokenRedisRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String accessToken = jwtUtil.resolveToken(request);
    String refreshToken = jwtUtil.resolveRefreshToken(request);

    if(accessToken != null) {
      checkLogout(accessToken);
      if (!jwtUtil.validateToken(accessToken)) {
        jwtExceptionHandler(response, "Token Error", 400);
        return;
      }
      Claims info = jwtUtil.getUserInfoFromToken(accessToken);
      setAuthentication(info.getSubject());
    }

    if(refreshToken != null) {
      checkRedis(refreshToken);
      if(!jwtUtil.validateToken(refreshToken)) {
        jwtExceptionHandler(response,"Token Error", 400);
        return;
      }
      Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
      setAuthentication(info.getSubject());
    }

    filterChain.doFilter(request, response);
  }


  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = jwtUtil.createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }


  public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
    response.setStatus(statusCode);
    response.setContentType("application/json");
    try {
      String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void checkLogout(String accessToken) {
    if (signoutAccessTokenRedisRepository.existsById(accessToken)) {
      throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
    }
  }

  private void checkRedis(String refreshToken) {
    if(refreshTokenRedisRepository.findById(jwtUtil.getUserInfoFromToken(refreshToken).getSubject()).isEmpty()) {
      throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
    }
  }




}

