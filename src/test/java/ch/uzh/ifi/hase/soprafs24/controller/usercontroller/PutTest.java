//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import ch.uzh.ifi.hase.soprafs24.controller.UserController;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//public class PutTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testUpdateUser() throws Exception {
//        // Mock existing user
//        User existingUser = new User();
//        existingUser.setId(1L);
//        existingUser.setUsername("testUser");
//        existingUser.setPassword("testPassword");
//
//        // Mock UserService behavior
//        when(userService.updateUser(any(), any())).thenReturn(existingUser);
//
//        // Create UserPutDTO
//        UserPutDTO userPutDTO = new UserPutDTO();
//        userPutDTO.setUsername("newUsername");
//        userPutDTO.setPassword("newPassword");
//
//        // Perform PUT request
//        mockMvc.perform(put("/users/" + existingUser.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtils.toJson(userPutDTO)))
//                .andExpect(status().isNoContent());
//    }
//
//    static class JsonUtils {
//        private static final ObjectMapper objectMapper = new ObjectMapper();
//
//        public static String toJson(Object object) {
//            try {
//                return objectMapper.writeValueAsString(object);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }
//}
