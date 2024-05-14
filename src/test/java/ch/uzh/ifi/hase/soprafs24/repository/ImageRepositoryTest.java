//package ch.uzh.ifi.hase.soprafs24.repository;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Image;
//import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
//import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ImageRepositoryTest {
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    @InjectMocks
//    private UnsplashService unsplashService;
//
//    @Test
//    void findImageById_ReturnsImage_WhenImageExists() {
//        // Given
//        Long imageId = 1L;
//        Image image = new Image();
//        image.setId(imageId);
//        when(imageRepository.findImageById(imageId)).thenReturn(image);
//
//        // When
//        Image found = imageRepository.findImageById(imageId);
//
//        // Then
//        assertEquals(imageId, found.getId());
//    }
//
//    @Test
//    void findImageById_ReturnsNull_WhenImageDoesNotExist() {
//        // Given
//        Long imageId = 1L;
//        when(imageRepository.findImageById(imageId)).thenReturn(null);
//
//        // When
//        Image found = imageRepository.findImageById(imageId);
//
//        // Then
//        assertEquals(null, found);
//    }
//}
