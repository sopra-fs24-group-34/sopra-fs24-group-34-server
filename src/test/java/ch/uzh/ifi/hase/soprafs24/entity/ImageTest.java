package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageTest {

    @Test
    public void testSetIdSuccess() {
        // Create an Image instance
        Image image = new Image();

        // Set id
        image.setId(1L);

        // Verify id
        assertEquals(1L, image.getId());
    }

    @Test
    public void testSetUrlSuccess() {
        // Create an Image instance
        Image image = new Image();

        // Set url
        String url = "https://example.com/image.jpg";
        image.setUrl(url);

        // Verify url
        assertEquals(url, image.getUrl());
    }

}
