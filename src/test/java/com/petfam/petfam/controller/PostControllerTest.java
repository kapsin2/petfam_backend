package com.petfam.petfam.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.amazonaws.services.kms.model.NotFoundException;
import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.dto.post.AllPostResponseDto;
import com.petfam.petfam.dto.post.PostCreateRequestDto;
import com.petfam.petfam.dto.post.PostResponseDto;
import com.petfam.petfam.dto.post.PostUpdateRequestDto;
import com.petfam.petfam.entity.Post;
import com.petfam.petfam.entity.User;
import com.petfam.petfam.entity.enums.CategoryEnum;
import com.petfam.petfam.entity.enums.UserRoleEnum;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.comment.CommentServiceImpl;
import com.petfam.petfam.service.post.PostServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

  @InjectMocks
  private PostController postController;

  @Mock
  private PostServiceImpl postService;

  @Mock
  private UserDetailsImpl userDetails;

  @Mock
  private CommentServiceImpl commentService;

  @Mock
  private HttpServletRequest httpServletRequest;

  @Mock
  private HttpServletResponse httpServletResponse;

  @Test
  @DisplayName("게시물생성")
  public void CreatePost() {
    // given
    PostCreateRequestDto.PostCreateRequestDtoBuilder postBuilder = PostCreateRequestDto.builder()
        .title("title")
        .content("content")
        .image("image")
        .category(CategoryEnum.PET);
    PostCreateRequestDto postCreateRequestDto = postBuilder.build();

    // when
    when(postService.createPost(postCreateRequestDto, userDetails.getUser())).thenReturn("Success");
    ResponseEntity<String> response = postController.createPost(postCreateRequestDto, userDetails);

    // then
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Success", response.getBody());
  }

  @Test
  @DisplayName("게시물 전체조회")
  void getAllPosts() {
    // given
    int page = 0;
    int size = 10;
    CategoryEnum category = CategoryEnum.PET;

    Pageable pageable = PageRequest.of(page, size);
    Page<AllPostResponseDto> expectedPage = new PageImpl<>(new ArrayList<>());
    when(postService.getPostsByCategory(category, pageable)).thenReturn(expectedPage);

    // when
    ResponseEntity<Page<AllPostResponseDto>> responseEntity = postController.getAllPosts(page, size, category);
    Page<AllPostResponseDto> result = responseEntity.getBody();

    // then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedPage, result);
    assertEquals(0, result.getContent().size());
  }

  @Test
  @DisplayName("게시물 페이징된거 가져오기")
  void getPosts() {
    // given
    int page = 0;
    int size = 10;
    CategoryEnum category = CategoryEnum.PET;

    // when
    Pageable pageable = PageRequest.of(page, size);
    when(postService.getPostsByCategory(category, pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
    ResponseEntity<Page<AllPostResponseDto>> response = postController.getPosts(page, size, category);
    Page<AllPostResponseDto> result = response.getBody();

    // then
    assertNotNull(result);
    assertEquals(0, result.getTotalElements());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("게시물 선택")
  void getSelectPost() {
    // given
    Long id = 1L;
    PostResponseDto postResponseDto = PostResponseDto.builder()
        .id(id)
        .title("title")
        .content("content")
        .build();
    when(postService.getSelectPost(id)).thenReturn(postResponseDto);

    // when
    ResponseEntity<PostResponseDto> response = postController.getSelectPost(id);
    PostResponseDto result = response.getBody();

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(result);
    assertEquals(id, result.getId());
    assertEquals("title", result.getTitle());
    assertEquals("content", result.getContent());
  }

  // 예외
  @DisplayName("게시물 선택 예외")
  @Test
  void getSelectPostException() {
    Long postId = 2L;
    String errorMessage = "게시물을 찾을 수 없습니다.";
    given(postService.getSelectPost(postId)).willThrow(new NotFoundException(errorMessage));

    assertThrows(NotFoundException.class, () -> postController.getSelectPost(postId), errorMessage);
  }

  @DisplayName("게시물 수정")
  @Test
  void updatePost() {
    // given
    Long postId = 1L;
    PostUpdateRequestDto postUpdateRequestDto = PostUpdateRequestDto.builder()
        .title("title")
        .content("content")
        .build();
    User user = User.builder()
        .id(1L)
        .username("user")
        .build();
    when(userDetails.getUser()).thenReturn(user);

    // when
    ResponseEntity<String> response = postController.updatePost(postId, postUpdateRequestDto, userDetails);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("게시글 수정이 완료되었습니다.", response.getBody());
    verify(postService, times(1)).updatePost(eq(postId), eq(postUpdateRequestDto), eq(user));
  }

  @Test
  @DisplayName("게시물 삭제")
  void deletePost() {
    // given
    Long postId = 1L;
    User user = User.builder()
        .id(1L)
        .username("user1")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();

    when(userDetails.getUser()).thenReturn(user);
    when(postService.deletePost(postId, user)).thenReturn("게시글 삭제가 완료되었습니다.");

    // when
    ResponseEntity<String> response = postController.deletePost(postId, userDetails);

    // then
    verify(userDetails, times(1)).getUser();
    verify(postService, times(1)).deletePost(postId, user);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("게시글 삭제가 완료되었습니다.", response.getBody());
  }

  @Test
  @DisplayName("게시물 삭제 예외")
  public void deletePostException() {
    Long postId = 1L;
    User user = User.builder()
        .id(1L)
        .username("user1")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();

    when(userDetails.getUser()).thenReturn(user);
    doThrow(new AccessDeniedException("Unauthorized")).when(postService).deletePost(postId, user);

    assertThrows(AccessDeniedException.class, () -> {
      postController.deletePost(postId, userDetails);
    });

    verify(userDetails, times(1)).getUser();
    verify(postService, times(1)).deletePost(postId, user);

    SecurityContextHolder.clearContext();
  }

  @DisplayName("댓글 생성")
  @Test
  void comment() {
    // given
    Long postId = 1L;
    CommentRequestDto.CommentRequestDtoBuilder commentBuilder = CommentRequestDto.builder().content("comment");
    CommentRequestDto commentRequestDto = commentBuilder.build();
    User user = User.builder()
        .id(1L)
        .username("user1")
        .password("password")
        .nickname("nickname")
        .userRole(UserRoleEnum.USER)
        .build();

    when(userDetails.getUser()).thenReturn(user);
    when(commentService.comment(postId, user, commentRequestDto)).thenReturn("success");

    // when
    ResponseEntity<String> responseEntity = postController.Comment(postId, commentRequestDto, userDetails);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals("success", responseEntity.getBody());
    verify(userDetails, times(1)).getUser();
    verify(commentService, times(1)).comment(postId, user, commentRequestDto);
  }
  @Test
  @DisplayName("조회수 중복방지")
  void updateView() {
    // given
    long id = 1L;

    // when
    PostController controller = new PostController(postService, commentService);
    controller.updateView(id, httpServletRequest, httpServletResponse);

    // then
    verify(postService, times(1)).updateView(eq(id), eq(httpServletRequest), eq(httpServletResponse));
  }
}