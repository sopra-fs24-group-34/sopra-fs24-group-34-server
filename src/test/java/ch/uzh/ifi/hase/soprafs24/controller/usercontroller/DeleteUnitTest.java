//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import ch.uzh.ifi.hase.soprafs24.controller.UserController;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = UserController.class)
//public class DeleteUnitTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    void deleteGuestUserUnitTest() throws Exception {
//        Long guestUserId = 1L;
//
//        Mockito.doNothing().when(userService).deleteUser(guestUserId);
//
//        mockMvc.perform(delete("/guestusers/{guestuserId}", guestUserId))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(userService, Mockito.times(1)).deleteUser(guestUserId);
//    }
//
//    @Test
//    void deleteUserUnitTest() throws Exception {
//        Long userId = 1L;
//
//        Mockito.doNothing().when(userService).deleteUser(userId);
//
//        mockMvc.perform(delete("/users/{userId}/delete", userId))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
//    }
//}
