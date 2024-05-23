package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void getAllUsers_success() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());

        Mockito.when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    public void getUser_success() throws Exception {
        User user = new User();
        user.setId(1L);

        Mockito.when(userService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
    }

    @Test
    public void createUser_success() throws Exception {
        User user = new User();
        AuthenticationDTO authDTO = new AuthenticationDTO();

        Mockito.when(userService.createUser(any(User.class))).thenReturn(authDTO);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void loginUser_success() throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO();

        Mockito.when(userService.loginUser(any(User.class))).thenReturn(authDTO);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void createGuestUser_success() throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO();

        Mockito.when(userService.createGuestUser(any(User.class))).thenReturn(authDTO);

        UserPostDTO userPostDTO = new UserPostDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/guestuser/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Guest\",\"password\":\"12345\"}"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void updateUser_success() throws Exception {
        User user = new User();
        user.setId(1L);

        Mockito.when(userService.updateUser(any(User.class), anyLong())).thenReturn(user);

        UserPutDTO userPutDTO = new UserPutDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\"}"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void deleteGuestUser_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/guestusers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void deleteUser_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

