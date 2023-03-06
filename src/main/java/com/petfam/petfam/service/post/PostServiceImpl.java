package com.petfam.petfam.service.post;

import com.petfam.petfam.dto.post.AllPostResponseDto;
import com.petfam.petfam.dto.post.PostCreateRequestDto;
import com.petfam.petfam.dto.post.PostResponseDto;
import com.petfam.petfam.dto.post.PostUpdateRequestDto;
import com.petfam.petfam.dto.post.PostUpdateResponseDto;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.CategoryEnum;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.repository.PostRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  public final static String VIEWCOOKIE = "alreadyViewCookie";

  @Override
  @Transactional
  public String createPost(PostCreateRequestDto requestDto, User user) {
    Post post = new Post(requestDto, user);
    postRepository.save(post);
    return "게시글 작성이 완료되었습니다.";
  }

  @Transactional(readOnly = true)
  @Override
  public Page<AllPostResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable) {
    Page<Post> posts = postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);

    List<AllPostResponseDto> allPostResponseDtoList = new ArrayList<>();

    if (posts.isEmpty()) {
      throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
    } else {
      for (Post post : posts) {
        AllPostResponseDto allPostResponseDto = new AllPostResponseDto(post);
        allPostResponseDtoList.add(allPostResponseDto);
      }
    }
    return new PageImpl<>(allPostResponseDtoList, pageable, posts.getTotalElements());
  }

  @Transactional(readOnly = true)
  @Override
  public PostResponseDto getSelectPost(Long postId) {
    Post post = _findPost(postId);
    return new PostResponseDto(post);
  }

  @Transactional
  @Override
  public PostUpdateResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
    Post post = _findPost(postId);

    if (user.getUserRole() != UserRoleEnum.ADMIN) {
      if (!post.getUser().getId().equals(user.getId())) {
        throw new IllegalArgumentException("글 작성자만 수정이 가능합니다.");
      }
    }

    post.updatePost(requestDto);
    return new PostUpdateResponseDto(post);
  }


  @Transactional
  @Override
  public String deletePost(Long postId, User user) {
    Post post = _findPost(postId);

    if (user.getUserRole() != UserRoleEnum.ADMIN) {
      if (!post.getUser().getId().equals(user.getId())) {
        throw new IllegalArgumentException("글 작성자만 삭제 가능합니다.");
      }
    }

    postRepository.deleteById(postId);
    return "게시글이 삭제되었습니다.";
  }

  // 중복 코드
  private Post _findPost(Long postId) {
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
    );
    return post;
  }

  // 조회수 증가
  @Transactional
  @Override
  public int updateView(Long id, HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();  // 클라이언트가 보낸 데이터에서 쿠키 찾기
    boolean checkCookie = false; // 쿠키값 정의
    int result = 0; // 조회수 0
    if (cookies != null) { // 쿠키가 있을 경우
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(VIEWCOOKIE + id)) { // 게시글의 쿠키인지 확인
          checkCookie = true;
        }
      }
    } else { // 쿠키가 없을 경우
      Cookie newCookie = _createCookieForNewView(id); // 새 쿠키 발급
      response.addCookie(newCookie);
      result = postRepository.updateView(id);
    }
    return result;
  }

  /**
   * 조회수 중복 방지를 위한 쿠키 생성 메소드
   */
  private Cookie _createCookieForNewView(Long postId) {
    Cookie cookie = new Cookie(VIEWCOOKIE + postId, String.valueOf(postId));
    cookie.setMaxAge(_getRemainTimeForTomorrow());
    cookie.setHttpOnly(true); // 서버에서만 조작 가능
    return cookie;
  }

  private int _getRemainTimeForTomorrow() {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime tomorrow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
    return (int) now.until(tomorrow, ChronoUnit.SECONDS);
  }
}
