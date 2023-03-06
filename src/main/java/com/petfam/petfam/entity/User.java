package com.petfam.petfam.entity;

import com.petfam.petfam.dto.user.ProfileUpdateDto;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// lombok
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) 테코 작업
@NoArgsConstructor

//jpa
@Entity(name = "users")
public class User {

  /**
   * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  private String image;

  private String introduction;

  private Integer point;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum userRole;

  private Long kakaoId;


  /**
   * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
   */

  @Builder
  public User(Long id, String username, String password, String nickname,
      String image, UserRoleEnum userRole) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.image = "src/main/java/resources/static/images/m_20220509173224_d9N4ZGtBVR.jpeg";
    this.introduction = "안녕하세요.";
    this.point = 0;
    this.userRole = userRole;
  }

  // id 생성자에 추가 -> 테스트코드를 위해서,이후 삭제 예정
  @Builder
  public User(String username, String password, String nickname,
      String image, UserRoleEnum userRole) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.image = "src/main/java/resources/static/images/m_20220509173224_d9N4ZGtBVR.jpeg";
    this.introduction = "안녕하세요.";
    this.point = 0;
    this.userRole = userRole;
  }

  public User(String username, Long kakaoId,String password ,String nickname) {
    this.username = username;
    this.password = password;
    this.kakaoId = kakaoId;
    this.nickname = nickname;
    this.image = "src/main/java/resources/static/images/m_20220509173224_d9N4ZGtBVR.jpeg";
    this.introduction = "안녕하세요.";
    this.point = 0;
    this.userRole = UserRoleEnum.USER;
  }


  /**
   * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
   */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ReComment> reComments = new ArrayList<>();

  /**
   * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
   */


  /**
   * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
   */
  // admin 검증 함수
  public boolean isAdmin() {
    return this.userRole == UserRoleEnum.ADMIN;
  }


  // 프로필 업데이트
  public void updateProfile(ProfileUpdateDto profileUpdateDto) {
    this.nickname = (profileUpdateDto.getNickname().equals("")) ? this.nickname : profileUpdateDto.getNickname();
    this.introduction = (profileUpdateDto.getIntroduction().equals("")) ? this.introduction : profileUpdateDto.getIntroduction();
    this.image = (profileUpdateDto.getImage().equals("")) ? this.image : profileUpdateDto.getImage();
  }


  public void setId(long l) {
    this.id = l;
  }

}
