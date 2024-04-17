package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long imageId;

    private String url;

    public Long getId() {
        return imageId;
    }

    public void setId(Long imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}