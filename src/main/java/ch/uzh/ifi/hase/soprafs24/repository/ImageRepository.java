package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageById(Long userId);

    @Query("SELECT COUNT(i) FROM Image i")
    int countAllImages();

    Boolean existsByNormalizedUrl(String normalizedUrl);
}