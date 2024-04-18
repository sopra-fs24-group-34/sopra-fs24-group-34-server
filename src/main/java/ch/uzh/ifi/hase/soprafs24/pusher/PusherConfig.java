package ch.uzh.ifi.hase.soprafs24.pusher;

import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
  @Value("${pusher.app_id}")
  private String appId;

  @Value("${pusher.key}")
  private String key;

  @Value("${pusher.secret}")
  private String secret;

  @Value("${pusher.cluster}")
  private String cluster;

  @Bean
  public Pusher pusher() {
    Pusher pusher = new Pusher(key, secret, appId);
    pusher.setCluster(cluster);
    return pusher;
  }
}