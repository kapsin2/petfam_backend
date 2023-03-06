package com.petfam.petfam.service.post;

import static com.petfam.petfam.service.post.PostServiceImpl.VIEWCOOKIE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petfam.petfam.dto.post.AllPostResponseDto;
import com.petfam.petfam.dto.post.PostCreateRequestDto;
import com.petfam.petfam.dto.post.PostResponseDto;
import com.petfam.petfam.dto.post.PostUpdateRequestDto;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.CategoryEnum;
import com.petfam.petfam.repository.PostRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

  @Mock
  PostRepository postRepository;

  @InjectMocks
  PostServiceImpl postService;


  @Test
  @DisplayName("게시글 생성 성공")
  void createPost() {
    // given
    PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
        .title("title1")
        .content("content1")
        .image("image1")
        .category(CategoryEnum.PET)
        .build();

    User user = mock(User.class);

    Post post = Post.builder()
        .requestDto(requestDto)
        .user(user)
        .build();

    given(postRepository.save(any())).willReturn(post);

    // when
    String savedPost = postService.createPost(requestDto, user);

    // then
    assertThat(savedPost).isEqualTo("게시글 작성이 완료되었습니다.");
  }

  @Nested
  @DisplayName("전체 게시글 불러오기")
  class getPosts {

    @Test
    @DisplayName("전체 게시글 조회")
    void getAllPosts() {
      // given
      CategoryEnum category = CategoryEnum.PET;
      Pageable pageable = PageRequest.of(0, 9);
      Post post1 = mock(Post.class);
      Post post2 = mock(Post.class);
      Post post3 = mock(Post.class);
      User user = mock(User.class);

      List<Post> postList = new ArrayList<>();
      postList.add(post1);
      postList.add(post2);
      postList.add(post3);
      Page<Post> postPage = new PageImpl<>(postList);

      when(post1.getUser()).thenReturn(user);
      when(post2.getUser()).thenReturn(user);
      when(post3.getUser()).thenReturn(user);
      when(user.getNickname()).thenReturn("user");

      // when
      when(postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)).thenReturn(
          postPage);
      Page<AllPostResponseDto> result = postService.getPostsByCategory(category, pageable);

      // then
      verify(postRepository, times(1)).findByCategoryOrderByCreatedAtDesc(category, pageable);
      assertEquals(3, result.getTotalElements());
      assertEquals(3, result.getContent().size());
    }

    @Test
    @DisplayName("게시글이 존재하지 않을 때")
    void noExistPosts() {
      // given
      CategoryEnum category = CategoryEnum.PET;
      Pageable pageable = PageRequest.of(0, 9);

      // when
      when(postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)).thenReturn(
          Page.empty());

      // then
      assertThrows(IllegalArgumentException.class,
          () -> postService.getPostsByCategory(category, pageable));
    }
  }


  @Nested
  @DisplayName("게시글 개별 조회")
  class getPost {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
          .title("title1")
          .content("content1")
          .image("image1")
          .category(CategoryEnum.PET)
          .build();

      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

      // when
      PostResponseDto responseDto = postService.getSelectPost(post.getId());

      // then
      assertThat(responseDto.getTitle()).isEqualTo(post.getTitle());
      assertThat(responseDto.getContent()).isEqualTo(post.getContent());
      assertThat(responseDto.getImage()).isEqualTo(post.getImage());
      assertThat(responseDto.getCategory().getName()).isEqualTo(post.getCategory().getName());
    }

    @Test
    @DisplayName("존재하지 않는 게시글의 경우")
    void getPostsNoExist() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      lenient().when(postRepository.findById(2L)).thenReturn(Optional.empty());

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> postService.getSelectPost(post.getId()));
      assertEquals("게시글이 존재하지 않습니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("게시글 업데이트")
  class updatePost {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
          .title("title1")
          .content("content1")
          .image("image1")
          .category(CategoryEnum.PET)
          .build();

      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

      PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
          .title("title11")
          .content("content11")
          .image("image11")
          .category(CategoryEnum.PET)
          .build();

      // when
      postService.updatePost(post.getId(), updateRequestDto, user);

      // then
      assertThat(updateRequestDto.getTitle()).isEqualTo(post.getTitle());
      assertThat(updateRequestDto.getContent()).isEqualTo(post.getContent());
      assertThat(updateRequestDto.getImage()).isEqualTo(post.getImage());
      assertThat(updateRequestDto.getCategory()).isEqualTo(post.getCategory());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시도")
    void updatePostWhatNoExist() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      PostUpdateRequestDto updateRequestDto = mock(PostUpdateRequestDto.class);

      lenient().when(postRepository.findById(2L)).thenReturn(Optional.empty());

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> postService.updatePost(post.getId(), updateRequestDto, user));
      assertEquals("게시글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("수정할 게시글의 작성자와 수정을 시도한 유저가 다른 경우")
    void updatePostAuthCheck() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);

      User user1 = mock(User.class);
      when(user1.getId()).thenReturn(1L);
      User user2 = mock(User.class);
      when(user2.getId()).thenReturn(2L);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user1)
          .build();

      PostUpdateRequestDto updateRequestDto = mock(PostUpdateRequestDto.class);

      when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> postService.updatePost(post.getId(), updateRequestDto, user2));
      assertEquals("글 작성자만 수정이 가능합니다.", exception.getMessage());
    }
  }


  @Nested
  @DisplayName("게시글 삭제")
  class deletePost {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

      // when
      postService.deletePost(post.getId(), user);

      // then
      verify(postRepository, times(1)).deleteById(post.getId());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 삭제 시도할 경우")
    void deletePostNotExist() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user)
          .build();

      lenient().when(postRepository.findById(2L)).thenReturn(Optional.empty());

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> postService.deletePost(post.getId(), user));
      assertEquals("게시글이 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    @DisplayName("삭제할 게시글의 작성자와 삭제를 시도한 유저가 다른 경우")
    void deletePostAuthCheck() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);

      User user1 = mock(User.class);
      when(user1.getId()).thenReturn(1L);
      User user2 = mock(User.class);
      when(user2.getId()).thenReturn(2L);

      Post post = Post.builder()
          .requestDto(requestDto)
          .user(user1)
          .build();

      when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

      // when&then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> postService.deletePost(post.getId(), user2));
      assertEquals("글 작성자만 삭제 가능합니다.", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("조회수 증가")
  class updateView {

    @Test
    @DisplayName("쿠키가 없을 경우")
    void updateViewWithNoCookies() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);
      Post post = Post.builder().requestDto(requestDto).user(user).build();

      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      Cookie[] cookies = null;
      int result = 1; // 기대하는 조회수 값

      Cookie newCookie = new Cookie(VIEWCOOKIE + post.getId(), String.valueOf(post.getId()));
      response.addCookie(newCookie);
      given(postRepository.updateView(post.getId())).willReturn(result);

      // when
      int view = postService.updateView(post.getId(), request, response);

      // then
      assertThat(view).isEqualTo(result);
    }

    @Test
    @DisplayName("게시글의 쿠키가 있을 경우")
    void updateViewWithCookies() {
      // given
      PostCreateRequestDto requestDto = mock(PostCreateRequestDto.class);
      User user = mock(User.class);
      Post post = Post.builder().requestDto(requestDto).user(user).build();

      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      Cookie[] cookies = {new Cookie(VIEWCOOKIE + post.getId(), "1")};
      when(request.getCookies()).thenReturn(cookies);

      // when
      int view = postService.updateView(post.getId(), request, response);

      // then
      assertEquals(0, view);
      verify(response, never()).addCookie(any(Cookie.class));
    }
  }
}
