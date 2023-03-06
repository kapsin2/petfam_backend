package com.petfam.petfam.controller;

import com.petfam.petfam.dto.user.UserResponseDto;
import com.petfam.petfam.service.user.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

	private final AdminService adminService;

	//페이징처리
	@GetMapping("/users")
	public ResponseEntity<Page<UserResponseDto>> getUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getUsers(pageable));
	}
}