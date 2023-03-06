package com.petfam.petfam.config;


import static com.petfam.petfam.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.petfam.petfam.jwt.JwtUtil.REFRESH_AUTHORIZATION_HEADER;

import com.petfam.petfam.jwt.JwtAuthFilter;
import com.petfam.petfam.jwt.JwtUtil;
import com.petfam.petfam.repository.RefreshTokenRedisRepository;
import com.petfam.petfam.repository.SignoutAccessTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {


  private final JwtUtil jwtUtil;
  private final SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();

    http.authorizeHttpRequests().requestMatchers("/users/signup").permitAll()

            .requestMatchers("/users/signin").permitAll()
            .requestMatchers("/users/admin/signup").permitAll()
            .requestMatchers("/users/admin/signin").permitAll()
            .requestMatchers("/users/refresh").permitAll()
            .requestMatchers("/users/login-page").permitAll()
            .requestMatchers("/users/id").permitAll()
            .requestMatchers("/users/nickname").permitAll()
            .requestMatchers("/users/kakao/callback").permitAll()
            .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
            .requestMatchers(HttpMethod.GET,"/posts").permitAll()
            .requestMatchers(HttpMethod.POST, "/posts/views/**").permitAll()  // 조회수용 쿠키 생성을 위한 허용
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtAuthFilter(jwtUtil, signoutAccessTokenRedisRepository, refreshTokenRedisRepository), UsernamePasswordAuthenticationFilter.class);
                

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.formLogin().disable();

    return http.build();
  }

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
        .allowedOrigins("http://localhost:8080", "https://petfam.netlify.app/", "http://127.0.0.1:5501/")
        .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS", "HEAD")
        .exposedHeaders(AUTHORIZATION_HEADER, REFRESH_AUTHORIZATION_HEADER)
        .allowCredentials(true)
        .maxAge(3600);
  }


}
