package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
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

//    @Test
//    public void testSaveRandomPortraitImagesToDatabase() {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, String> urls = new HashMap<>();
//        urls.put("regular", "https://images.unsplash.com/photo-1234");
//        response.put("urls", urls);
//        Map<String, Object>[] responses = new Map[]{response};
//
//        when(restTemplate.getForObject(anyString(), eq(Map[].class))).thenReturn(responses);
//        when(imageRepository.existsByNormalizedUrl(anyString())).thenReturn(false);
//
//        unsplashService.saveRandomPortraitImagesToDatabase(1);
//
//        verify(imageRepository, times(1)).save(any(Image.class));
//        verify(imageRepository, times(1)).flush();
//    }

//    @Test
//    public void testSaveRandomPortraitImagesToDatabase_AlreadyExists() {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, String> urls = new HashMap<>();
//        urls.put("regular", "https://images.unsplash.com/photo-1234");
//        response.put("urls", urls);
//        Map<String, Object>[] responses = new Map[]{response};
//
//        when(restTemplate.getForObject(anyString(), eq(Map[].class))).thenReturn(responses);
//        when(imageRepository.existsByNormalizedUrl(anyString())).thenReturn(true);
//
//        unsplashService.saveRandomPortraitImagesToDatabase(1);
//
//        verify(imageRepository, never()).save(any(Image.class));
//    }

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
}


//package ch.uzh.ifi.hase.soprafs24.service;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Image;
//import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UnsplashServiceTest {
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    @InjectMocks
//    private UnsplashService unsplashService;
//
//    @Value("${unsplash.api.accessKey}")
//    private String accessKey;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void saveRandomPortraitImagesToDatabase_WithValidCount_ShouldSaveImages() {
////        // Mocking
////        RestTemplate restTemplate = mock(RestTemplate.class);
////        String url = "https://api.unsplash.com/photos/random?client_id=ACCESS_KEY&orientation=portrait&count=5&page=1";
////        Map<String, Object>[] responses = new Map[5]; // Simulate 5 responses
////        when(restTemplate.getForObject(url, Map[].class)).thenReturn(responses);
////
////        // Stubbing
////        when(imageRepository.existsByNormalizedUrl(anyString())).thenReturn(false); // Assume no images exist
////
////        // Test
////        unsplashService.saveRandomPortraitImagesToDatabase(5);
////
////        // Verify
////        verify(imageRepository, times(5)).save(any(Image.class)); // Verify save method is called 5 times
////    }
//
//    @Test
//    void getImageUrlsFromDatabase_validInput() {
//        // mock the image repository findAll method
//        List<Image> images = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Image image = new Image();
//            image.setUrl("https://example.com/image" + i);
//            images.add(image);
//        }
//        when(imageRepository.findAll()).thenReturn(images);
//
//        List<ImageDTO> result = unsplashService.getImageUrlsFromDatabase(5, Optional.empty());
//
//        assertEquals(5, result.size());
//    }
//
//    @Test
//    void getImageUrlsFromDatabase_handleException() {
//        // mock the image repository findAll method to throw an exception
//        when(imageRepository.findAll()).thenThrow(new RuntimeException("Database error"));
//
//        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
//                () -> unsplashService.getImageUrlsFromDatabase(5, Optional.empty()));
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//    }
//
//}
//
//
////package ch.uzh.ifi.hase.soprafs24.service;
////
////import ch.uzh.ifi.hase.soprafs24.entity.Image;
////import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
////import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.extension.ExtendWith;
////import org.mockito.InjectMocks;
////import org.mockito.Mock;
////import org.mockito.Mockito;
////import org.mockito.junit.jupiter.MockitoExtension;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.http.HttpStatus;
////import org.springframework.web.server.ResponseStatusException;
////
////
////import java.util.ArrayList;
////import java.util.List;
////import java.util.Optional;
////
////import static org.junit.jupiter.api.Assertions.assertEquals;
////import static org.mockito.ArgumentMatchers.any;
////import static org.mockito.Mockito.*;
////
////
////
////@ExtendWith(MockitoExtension.class)
////public class UnsplashServiceTest {
////
////    @Mock
////    private ImageRepository imageRepository;
////
////    @InjectMocks
////    private UnsplashService unsplashService;
////
////    @Value("${unsplash.api.accessKey}")
////    private String accessKey;
////
////
////    @BeforeEach
////    public void setup() {
////    }
////
//////    @Test
//////    void saveRandomPortraitImagesToDatabase_validInput() {
//////        // define the count of images to be saved
//////        int count = 3;
//////
//////        String accessKey = "paX_Qx0iuUJT8__Xk6ivEA4QnJQlTbc-UtMtArGYxis";
//////        unsplashService.setAccessKey(accessKey);
//////
//////        // mock the imageRepository save method and return a mock Image object
//////        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));
//////
//////        unsplashService.saveRandomPortraitImagesToDatabase(count);
//////
//////        // verify that the save method was called 3 times
//////        verify(imageRepository, times(count)).save(any(Image.class));
//////    }
////
////
////
////    @Test
////    void getImageUrlsFromDatabase_validInput() {
////        // mock the image repository findAll method
////        List<Image> images = new ArrayList<>();
////        for (int i = 0; i < 10; i++) {
////            Image image = new Image();
////            image.setUrl("https://example.com/image" + i);
////            images.add(image);
////        }
////        when(imageRepository.findAll()).thenReturn(images);
////
////        List<ImageDTO> result = unsplashService.getImageUrlsFromDatabase(5, Optional.empty());
////
////        assertEquals(5, result.size());
////    }
////
////    @Test
////    void getImageUrlsFromDatabase_handleException() {
////        // mock the image repository findAll method to throw an exception
////        when(imageRepository.findAll()).thenThrow(new RuntimeException("Database error"));
////
////        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class,
////                () -> unsplashService.getImageUrlsFromDatabase(5, Optional.empty()));
////
////        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
////    }
////}
