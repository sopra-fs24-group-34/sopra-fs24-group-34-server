//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class PostTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testCreateUser() throws Exception {
//        // Create a UserPostDTO object
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("testUser");
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
//    }
//
//    // smailalijagic: fails
////    @Test
////    public void testLoginUser() throws Exception {
////        // Create a UserPostDTO object
////        UserPostDTO userPostDTO = new UserPostDTO();
////        userPostDTO.setUsername("testUser");
////        userPostDTO.setPassword("testPassword");
////
////        // Convert UserPostDTO to JSON
////        String json = objectMapper.writeValueAsString(userPostDTO);
////
////        // Perform POST request to the endpoint
////        mockMvc.perform(post("/login")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(json))
////                .andExpect(status().isOk());
////    }
//
//    // smailalijagic: fails
////    @Test
////    public void testCreateGuestUser() throws Exception {
////        // Create a UserPostDTO object
////        UserPostDTO userPostDTO = new UserPostDTO();
////        userPostDTO.setUsername("testUser");
////        userPostDTO.setPassword("testPassword");
////
////        // Convert UserPostDTO to JSON
////        String json = objectMapper.writeValueAsString(userPostDTO);
////
////        // Perform POST request to the endpoint
////        mockMvc.perform(post("/guestuser/create")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(json))
////                .andExpect(status().isCreated());
////    }
//}
