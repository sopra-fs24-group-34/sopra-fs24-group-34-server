package ch.uzh.ifi.hase.soprafs24.service.unsplashService;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UnsplashServiceTest {

    @InjectMocks
    private UnsplashService unsplashService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        setPrivateField(unsplashService, "accessKeysString", "dummyKey1,dummyKey2");
        unsplashService.init();
    }

    private void setPrivateField(Object target, String fieldName, String value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object invokePrivateMethod(Object target, String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }


    @Test
    public void testSaveRandomPortraitImagesToDatabase_Exception() {
        when(restTemplate.getForObject(anyString(), eq(Map[].class))).thenThrow(new RuntimeException("API rate limit exceeded"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            unsplashService.saveRandomPortraitImagesToDatabase(1);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void testNormalizeUrl() throws Exception {
        String url = "https://images.unsplash.com/photo-1234&ixid=1234";
        String normalizedUrl = (String) invokePrivateMethod(unsplashService, "normalizeUrl", new Class<?>[]{String.class}, url);
        assertEquals("https://images.unsplash.com/photo-1234", normalizedUrl);
    }

    @Test
    public void testGetImageUrlsFromDatabase() {
        List<Image> images = new ArrayList<>();
        Image image1 = new Image();
        image1.setId(1L);
        image1.setUrl("https://images.unsplash.com/photo-1");
        Image image2 = new Image();
        image2.setId(2L);
        image2.setUrl("https://images.unsplash.com/photo-2");
        images.add(image1);
        images.add(image2);

        when(imageRepository.findAll()).thenReturn(images);

        List<ImageDTO> imageDTOs = unsplashService.getImageUrlsFromDatabase(2, Optional.empty());

        assertEquals(2, imageDTOs.size());
        List<String> expected_images = new ArrayList<>();
        expected_images.add(image1.getUrl());
        expected_images.add(image2.getUrl());
        List<String> actual_images = new ArrayList<>();
        actual_images.add(imageDTOs.get(0).getUrl());
        actual_images.add(imageDTOs.get(1).getUrl());

        assertTrue(expected_images.containsAll(actual_images) && actual_images.containsAll(expected_images));
    }

    @Test
    public void testGetImageUrlsFromDatabase_WithGameImages() {
        List<Image> images = new ArrayList<>();
        Image image1 = new Image();
        image1.setId(1L);
        image1.setUrl("https://images.unsplash.com/photo-1");
        Image image2 = new Image();
        image2.setId(2L);
        image2.setUrl("https://images.unsplash.com/photo-2");
        images.add(image1);
        images.add(image2);

        when(imageRepository.findAll()).thenReturn(images);

        List<ImageDTO> gameImages = new ArrayList<>();
        gameImages.add(new ImageDTO(1L, "https://images.unsplash.com/photo-1"));

        List<ImageDTO> imageDTOs = unsplashService.getImageUrlsFromDatabase(1, Optional.of(gameImages));

        assertEquals(1, imageDTOs.size());
        assertEquals("https://images.unsplash.com/photo-2", imageDTOs.get(0).getUrl());
    }

    @Test
    public void testGetImageUrlsFromDatabase_Exception() {
        when(imageRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(ResponseStatusException.class, () -> {
            unsplashService.getImageUrlsFromDatabase(1, Optional.empty());
        });
    }

    @Test
    public void testSaveRandomPortraitImagesToDatabase_ExceptionHandling() {
        unsplashService.init();
        when(restTemplate.getForObject(anyString(), eq(Map[].class))).thenThrow(new RuntimeException("Test exception"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            unsplashService.saveRandomPortraitImagesToDatabase(1);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Error while saving images to database - RATE EXCEEDED", exception.getReason());
    }

}
