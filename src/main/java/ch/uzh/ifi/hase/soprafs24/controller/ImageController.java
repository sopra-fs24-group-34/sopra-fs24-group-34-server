package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/images/fetch-and-save")
    public String fetchAndSaveImages() {
        unsplashService.saveRandomPortraitImagesToDatabase(5); //should be 20, for now 5 because of the api limitation
        return "Successfully fetched and saved images to the database.";
    }
    @GetMapping("/images/random")
    public String getRandomImage() {
        List<String> imageUrls = unsplashService.getAllImageUrlsFromDatabase();
        if (!imageUrls.isEmpty()) {
            int randomIndex = (int) (Math.random() * imageUrls.size());
            return imageUrls.get(randomIndex);
        } else {
            return "No images found in the database.";
        }
    }
}