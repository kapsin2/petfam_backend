package com.petfam.petfam.controller;


//import com.petfam.petfam.service.user.AdminServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ExtendWith(MockitoExtension.class)
//public class AdminControllerTest {
//
//  @Mock
//  private AdminServiceImpl adminService;
//
//  @InjectMocks
//  private AdminController adminController;
//
//  private MockMvc mockMvc;
//
//  @BeforeEach
//  public void init() {
//    mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
//  }
//
//
////  @Test
////  void getUsers() throws Exception {
////    // giver
////    UserResponseDto responseDto1 = new UserResponseDto(1L,"user1","kap1","ROLE_USER");
////    UserResponseDto responseDto2 = new UserResponseDto(2L,"user2","kap2","ROLE_USER");
////    int page = 0;
////    int size = 10;
////    Pageable pageable = PageRequest.of(0,10);
////    Page<UserResponseDto> users = new PageImpl<>(Arrays.asList(responseDto1,responseDto2),pageable,2);
////    given(adminService.getUsers(any(Pageable.class))).willReturn(users);
////
////    // when
////    MvcResult mvcResult = mockMvc.perform(get("/admin/users"))
////        .andExpect(status().isOk())
////        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
////        .andReturn();
////
////    // then
////    resultActions.andExpect(status().isOk())
////        .andExpect()
////
////  }
//}


