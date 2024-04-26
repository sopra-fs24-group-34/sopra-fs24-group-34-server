package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import java.util.ArrayList;
import java.util.List;
@RestController
public class ImageController {

    private final UnsplashService unsplashService;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(UnsplashService unsplashService, ImageRepository imageRepository) {
        this.unsplashService = unsplashService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/images/saving")
    public String fetchAndSaveImages() { //fetches from database and saves to database
        unsplashService.saveRandomPortraitImagesToDatabase(200);
        return "Successfully fetched and saved images to the database.";
    }
    @GetMapping("/images")
    public List<ImageDTO> getImageUrls(@RequestParam(defaultValue = "20") int count) {
        //gets images from database
        return unsplashService.getImageUrlsFromDatabase(count);
    }
    @DeleteMapping("/images/{imageId}")
    public void deleteImage(@PathVariable Long imageId) {
        UnsplashService.deleteImage(imageId, imageRepository);
    }
}