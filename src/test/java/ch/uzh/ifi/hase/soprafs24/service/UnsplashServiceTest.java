package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class UnsplashServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private UnsplashService unsplashService;

    @Value("${unsplash.api.accessKey}")
    private String accessKey;


    @BeforeEach
    public void setup() {
    }

    @Test
    void saveRandomPortraitImagesToDatabase_validInput() {
        // define the count of images to be saved
        int count = 3;

        String accessKey = "paX_Qx0iuUJT8__Xk6ivEA4QnJQlTbc-UtMtArGYxis";
        unsplashService.setAccessKey(accessKey);

        // mock the imageRepository save method and return a mock Image object
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        unsplashService.saveRandomPortraitImagesToDatabase(count);

        // verify that the save method was called 3 times
        verify(imageRepository, times(count)).save(any(Image.class));
    }



    @Test
    void getImageUrlsFromDatabase_validInput() {
        // mock the image repository findAll method
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Image image = new Image();
            image.setUrl("https://example.com/image" + i);
            images.add(image);
        }
        when(imageRepository.findAll()).thenReturn(images);

        List<ImageDTO> result = unsplashService.getImageUrlsFromDatabase(5, Optional.empty());

        assertEquals(5, result.size());
    }

    @Test
    void getImageUrlsFromDatabase_handleException() {
        // mock the image repository findAll method to throw an exception
        when(imageRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
                () -> unsplashService.getImageUrlsFromDatabase(5, Optional.empty()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
