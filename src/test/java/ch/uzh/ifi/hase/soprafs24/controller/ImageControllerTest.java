package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UnsplashService unsplashService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testFetchAndSaveImages() throws Exception {
        doNothing().when(unsplashService).saveRandomPortraitImagesToDatabase(150);

        mockMvc.perform(post("/images/saving"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully fetched and saved images to the database."));

        verify(unsplashService).saveRandomPortraitImagesToDatabase(150);
    }
}
