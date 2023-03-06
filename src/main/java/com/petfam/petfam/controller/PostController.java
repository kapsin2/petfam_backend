package com.petfam.petfam.controller;

import com.petfam.petfam.dto.comment.CommentRequestDto;
import com.petfam.petfam.dto.post.AllPostResponseDto;
import com.petfam.petfam.dto.post.PostCreateRequestDto;
import com.petfam.petfam.dto.post.PostResponseDto;
import com.petfam.petfam.dto.post.PostUpdateRequestDto;
import com.petfam.petfam.entity.enums.CategoryEnum;
import com.petfam.petfam.security.UserDetailsImpl;
import com.petfam.petfam.service.comment.CommentService;
import com.petfam.petfam.service.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
  public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 게시글 작성
    @PostMapping("")
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
      String post = postService.createPost(postCreateRequestDto, userDetails.getUser());
      return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
    //카테고리 미적용
    @GetMapping("/all")
    public ResponseEntity<Page<AllPostResponseDto>> getAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) CategoryEnum category) {
      Pageable pageable;
      if (size != null) {
        pageable = PageRequest.of(page, size);
      } else {
        pageable = Pageable.unpaged();
      }
      Page<AllPostResponseDto> allPosts = postService.getPostsByCategory(category, pageable);
      return ResponseEntity.ok(allPosts);
    }


  // 게시글 전체 목록 조회
  @GetMapping("")
  public ResponseEntity<Page<AllPostResponseDto>> getPosts(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "9") int size,

      @RequestParam(required = false) CategoryEnum category) {
    Pageable pageable = PageRequest.of(page, size);
    Page<AllPostResponseDto> posts = postService.getPostsByCategory(category, pageable);
    return ResponseEntity.ok(posts);
  }

  // 선택 게시글 조회
  @GetMapping("/{id}")
  public ResponseEntity<PostResponseDto> getSelectPost(@PathVariable Long id) {
    PostResponseDto post = postService.getSelectPost(id);
    return ResponseEntity.ok(post);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> updatePost(@PathVariable Long id,
      @RequestBody PostUpdateRequestDto postUpdateRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.updatePost(id, postUpdateRequestDto, userDetails.getUser());
    return ResponseEntity.ok("게시글 수정이 완료되었습니다.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deletePost(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.deletePost(id, userDetails.getUser());
    return ResponseEntity.ok("게시글 삭제가 완료되었습니다.");
  }

  // 댓글 생성
  @PostMapping("/{postId}/comments")
  public ResponseEntity<String> Comment(@PathVariable Long postId,
      @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.comment(postId, userDetails.getUser(), commentRequestDto));
  }

  // 조회수 중복 방지용 쿠키 발행
  @PostMapping("/views/{id}")
  public void updateView(@PathVariable long id,
      HttpServletRequest request,
      HttpServletResponse response) {
    postService.updateView(id, request, response);
  }
}
