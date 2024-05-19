package ch.uzh.ifi.hase.soprafs24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.ComponentScan;


@RestController
@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = {"ch.uzh.ifi.hase.soprafs24"})
public class Application {
  public static void main(String[] args) {SpringApplication.run(Application.class, args);}

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String helloWorld() {return "The application is running.";}

  @Bean
  public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              registry.addMapping("/**")
                      .allowedOrigins("http://localhost:3000", "https://sopra-fs24-group-34-client.oa.r.appspot.com")
                      .allowedMethods("*")
                      .allowCredentials(true)
                      .allowedHeaders("*");

              // Allow WebSocket connections
              registry.addMapping("/ws/**")
                      .allowedOriginPatterns("ws://localhost:*", "wss://localhost:*",
                              "http://localhost:*", "https://localhost:*",
                              "ws://sopra-fs24-group-34-*", "wss://sopra-fs24-group-34-*",
                              "http://sopra-fs24-group-34-*", "https://sopra-fs24-group-34-*")
                      .allowedMethods("*")
                      .allowCredentials(true)
                      .allowedHeaders("*");

          }
      };
  }
}
