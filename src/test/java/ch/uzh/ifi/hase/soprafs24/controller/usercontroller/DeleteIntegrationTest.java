//package ch.uzh.ifi.hase.soprafs24.controller.usercontroller;
//
//import ch.uzh.ifi.hase.soprafs24.Application;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
//public class DeleteIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    void deleteGuestUserIntegrationTest() {
//        Long guestUserId = 1L;
//
//        userService.deleteUser(guestUserId);
//
//        ResponseEntity<Void> response = restTemplate.exchange(
//                "http://localhost:" + port + "/guestusers/{guestuserId}",
//                HttpMethod.DELETE,
//                null,
//                Void.class,
//                guestUserId);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//    }
//
//    @Test
//    void deleteUserIntegrationTest() {
//        Long userId = 1L;
//
//        userService.deleteUser(userId);
//
//        ResponseEntity<Void> response = restTemplate.exchange(
//                "http://localhost:" + port + "/users/{userId}/delete",
//                HttpMethod.DELETE,
//                null,
//                Void.class,
//                userId);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//    }
//}
//
