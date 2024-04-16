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
import java.util.Random;
import java.util.logging.Logger;
import java.util.Collections;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;



@Service
public class UnsplashService {
    private static final Logger logger = Logger.getLogger(UnsplashService.class.getName());


    @Autowired
    private ImageRepository imageRepository;

    @Value("${unsplash.api.accessKey}")
    private String accessKey;

    public void saveRandomPortraitImagesToDatabase(int count) {
    try {
        String url = "https://api.unsplash.com/photos/random?client_id=" + accessKey +
                "&orientation=portrait&count=" + count + "&query=people";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object>[] responses = restTemplate.getForObject(url, Map[].class);

        if (responses != null) {
            for (Map<String, Object> response : responses) {
                Map<String, String> urls = (Map<String, String>) response.get("urls");
                String imageUrl = urls.get("regular");

                Image image = new Image();
                image.setUrl(imageUrl);
                imageRepository.save(image);
            }
        }
    } catch (Exception e) {
            // Log any exceptions
            logger.severe("Error while saving images to database: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving images to database");}
    }

    /* call the following function like:
       const response = await api.get("/images", {
          params: {
              count: 20 // Pass the count parameter to fetch 20 images
          }
        }
     */
    public List<ImageDTO> getImageUrlsFromDatabase(int count) {
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
                if (!isDuplicate) { //add tuple of image and id to returned array
                    imageUrls.add(new ImageDTO(image.getId(), imageUrl));
                }
            }
            return imageUrls;
        } catch (Exception e) {
            // Log any exceptions
            logger.severe("Error while retrieving image URLs from database: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while retrieving image URLs from database");
        }
    }
}
