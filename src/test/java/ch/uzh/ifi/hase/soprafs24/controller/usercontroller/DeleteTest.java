//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDeleteDTO;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class DeleteTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private UserRepository userRepository;
//
//    @Test
//    public void testDeleteUser() throws Exception {
////        // Create a UserPostDTO object
////        UserPostDTO userPostDTO = new UserPostDTO();
////        userPostDTO.setUsername("testUser");
////        userPostDTO.setPassword("testPassword");
////
////        // Convert UserPostDTO to JSON
////        String json = objectMapper.writeValueAsString(userPostDTO);
////
////        // Perform POST request to the endpoint
////        mockMvc.perform(post("/register")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(json))
////                .andExpect(status().isCreated());
//
//        User user = new User();
//        user.setPassword("testPassword");
//        user.setUsername("testUsername");
//        user.setId(1L);
//        user.setStatus(UserStatus.ONLINE);
//
//        userRepository.save(user);
//
//        String json = objectMapper.writeValueAsString(user);
//
//        // Perform DELETE request to the endpoint
//        mockMvc.perform(delete("/users/1/delete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void testDeleteGuestUser() throws Exception {
//        // Create a UserPostDTO object
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("GuestUser");
//        userPostDTO.setPassword("testPassword");
//
//        // Convert UserPostDTO to JSON
//        String json = objectMapper.writeValueAsString(userPostDTO);
//
//        // Perform POST request to the endpoint
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isCreated());
//
//        // Perform DELETE request to the endpoint
//        mockMvc.perform(delete("/guestusers/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isNoContent());
//    }
//}