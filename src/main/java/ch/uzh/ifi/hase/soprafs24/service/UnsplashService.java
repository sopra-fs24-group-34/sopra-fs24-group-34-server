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
        }
    }

    public List<String> getAllImageUrlsFromDatabase() {
        try {
            List<String> imageUrls = new ArrayList<>();
            List<Image> images = imageRepository.findAll();
            for (Image image : images) {
                imageUrls.add(image.getUrl());
            }
            return imageUrls;
        } catch (Exception e) {
            // Log any exceptions
            logger.severe("Error while retrieving image URLs from database: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Image createimage() {
        Image image = new Image();

        image = imageRepository.save(image);
        imageRepository.flush();

        return image;
    }

    public Image getimage(Long imageid) {
        return imageRepository.findByImageId(imageid);
    }
}
