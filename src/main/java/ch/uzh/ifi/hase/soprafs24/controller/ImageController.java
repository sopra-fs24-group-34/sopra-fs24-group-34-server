package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/image/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Image createimage(){
    // method to check with postman
    return unsplashService.createimage();
  }

  @GetMapping("/image/{imageId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Image getImage(@PathVariable("imageId") Long imageid){
    // method to check with postman
    return unsplashService.getimage(imageid);
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