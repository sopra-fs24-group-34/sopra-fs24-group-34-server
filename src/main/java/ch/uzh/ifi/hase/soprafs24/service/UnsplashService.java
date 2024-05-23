package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.Random;
import java.util.logging.Logger;
import java.util.Collections;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;


@Service
@Transactional
public class UnsplashService {
    private static final Logger logger = Logger.getLogger(UnsplashService.class.getName());


  @Autowired
  private ImageRepository imageRepository;

    private int page = 1;
    // global page variable to maintain state across calls

    @Value("${unsplash.api.accessKey}")
  private String accessKeysString;

    private List<String> accessKeys;
    private int currentKeyIndex = 0;
    private int requestsMade = 0;
    private static final int REQUEST_LIMIT = 49;

    @PostConstruct
    public void init() {
        accessKeys = List.of(accessKeysString.split(","));
    }
    private void switchToNextKey() {
        currentKeyIndex = (currentKeyIndex + 1) % accessKeys.size();
        requestsMade = 0;
    }

    // Method to get the current access key
    private String getCurrentAccessKey() {
        if (requestsMade >= REQUEST_LIMIT) {
            switchToNextKey();
        }
        return accessKeys.get(currentKeyIndex);
    }


    public void saveRandomPortraitImagesToDatabase(int count) {
        try {
            int imagesFetched = 0;
            while (imagesFetched < count) {

                String url = "https://api.unsplash.com/photos/random?client_id=" + getCurrentAccessKey() +
                        "&orientation=portrait&count=" + (count - imagesFetched) + "&query=people&page=" + page;
                requestsMade++;
                logger.info("Fetching images from: " + url); // Add logging

                RestTemplate restTemplate = new RestTemplate();
                Map<String, Object>[] responses = restTemplate.getForObject(url, Map[].class);

                if (responses != null) {
                    for (Map<String, Object> response : responses) {
                        Map<String, String> urls = (Map<String, String>) response.get("urls");
                        String imageUrl = urls.get("regular");
                        String normalizedUrl = normalizeUrl(imageUrl); // normalize the URL

                        if (!imageRepository.existsByNormalizedUrl(normalizedUrl)) { // check by normalized URL
                            Image image = new Image();
                            image.setUrl(imageUrl);
                            image.setNormalizedUrl(normalizedUrl);
                            imageRepository.save(image);
                            imageRepository.flush();
                            imagesFetched++;
                            logger.info("Image saved to database. Total images fetched: " + imagesFetched);
                        }
                        else {
                            logger.info("Image already exists in the database: " + normalizedUrl); // Add logging
                        }
                    }

                }
                Random random = new Random();
                page = random.nextInt(20) + 1;
            }
        } catch (Exception e) {
            // Log any exceptions
            logger.severe("Error while saving images to database: " + e.getMessage());
            switchToNextKey();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error while saving images to database - RATE EXCEEDED");}
    }

    private String normalizeUrl(String url) {
        // Find the index of the parameter to ignore
        int index = url.indexOf("&ixid=");
        if (index != -1) {
            return url.substring(0, index).trim().toLowerCase();
        }
        return url.trim().toLowerCase();
    }

    /* call the following function like:
       const response = await api.get("/images", {
          params: {
              count: 20 // Pass the count parameter to fetch 20 images
          }
        }
     */
    public List<ImageDTO> getImageUrlsFromDatabase(int count, Optional<List<ImageDTO>> gameImages) {
        try {

            List<ImageDTO> imageUrls = new ArrayList<>();
            List<Image> images = imageRepository.findAll(); //add all images
            Collections.shuffle(images); // randomize order of images

            for (Image image : images) { //add a specific amount of images
                if (imageUrls.size() >= count) {
                    break;
                }
                String imageUrl = image.getUrl(); //url of new image
                boolean isDuplicate = false;

                // Check if the URL already exists in the list of ImageDTO objects

                for (ImageDTO object : imageUrls) {
                    if (imageUrl.equals(object.getUrl())) { // url of new image in array?
                        isDuplicate = true;
                        break;
                    }
                }
                if (gameImages.isPresent()) {
                    List<ImageDTO> gameImagesList = gameImages.get();
                    for (ImageDTO object : gameImagesList) {
                        if (imageUrl.equals(object.getUrl())) { // url of new image in array?
                            isDuplicate = true;
                            break;
                        }
                    }
                }
                if (!isDuplicate) { //add tuple of image and id to returned array
                    imageUrls.add(new ImageDTO(image.getId(), imageUrl));
                }
            }

            return imageUrls;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while retrieving image URLs from database");
        }
    }
}