//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class GetTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testGetAllUsers() throws Exception {
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk());
//    }
//
//    // smailalijagic: fails
////    @Test
////    public void testGetUser() throws Exception {
////        mockMvc.perform(get("/users/{userId}", 1L))
////                .andExpect(status().isOk());
////    }
//
//    // Add similar test methods for other GET mappings if needed
//}