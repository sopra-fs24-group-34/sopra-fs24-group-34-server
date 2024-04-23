
package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationResponseDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void createUser_register_valid() throws Exception {
    // given
    AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
    authenticationResponseDTO.setId(1L);
    authenticationResponseDTO.setToken("1");

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("Test User");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(authenticationResponseDTO);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(authenticationResponseDTO.getId().intValue())))
        .andExpect(jsonPath("$.token", is(authenticationResponseDTO.getToken())));
  }

    @Test
    public void createUser_login_valid() throws Exception {
        // given
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setId(1L);
        authenticationResponseDTO.setToken("1");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.loginUser(Mockito.any())).willReturn(authenticationResponseDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authenticationResponseDTO.getId().intValue())))
                .andExpect(jsonPath("$.token", is(authenticationResponseDTO.getToken())));
    }

    @Test
    public void createGuestUser_valid() throws Exception {
        // given
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO();
        authenticationResponseDTO.setId(1L);
        authenticationResponseDTO.setToken("1");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.loginUser(Mockito.any())).willReturn(authenticationResponseDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/guestuser/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authenticationResponseDTO.getId().intValue())))
                .andExpect(jsonPath("$.token", is(authenticationResponseDTO.getToken())));
    }

    @Test
    public void updateUser_valid() throws Exception {
        // given
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(1L);
        userGetDTO.setPassword("Test User");
        userGetDTO.setUsername("testUsername");
        userGetDTO.setStatus(UserStatus.ONLINE);
        userGetDTO.setToken("1");

        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setPassword("Test User");
        userPutDTO.setUsername("testUsername");
        userPutDTO.setStatus(UserStatus.ONLINE);
        userPutDTO.setToken("1");

        given(userService.updateUser(Mockito.any(), Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(userPutDTO.getId().intValue())))
                .andExpect(jsonPath("$.token", is(userPutDTO.getToken())))
                .andExpect(jsonPath("$.username", is(userPutDTO.getUsername())))
                .andExpect(jsonPath("$.password", is(userPutDTO.getPassword())));
    }

    @Test
    public void deleteGuestUser_valid() throws Exception {

    }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   *
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}

