package com.spring.reactive.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class MoviesInfoServiceApplication {
  public static void main(String[] args) {

    SpringApplication.run(MoviesInfoServiceApplication.class, args);
  }
}
