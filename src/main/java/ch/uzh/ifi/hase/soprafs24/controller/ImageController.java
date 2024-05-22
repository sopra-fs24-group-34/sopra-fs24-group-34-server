package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageController {

    private final UnsplashService unsplashService;

    @Autowired
    public ImageController(UnsplashService unsplashService) {
        this.unsplashService = unsplashService;
    }

    @PostMapping("/images/saving")
    public String fetchAndSaveImages() { //fetches from database and saves to database
        unsplashService.saveRandomPortraitImagesToDatabase(150);
        return "Successfully fetched and saved images to the database.";
    }

}