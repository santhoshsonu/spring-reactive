package com.spring.reactive.movies.controller;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.service.MoviesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/movies/info")
public class MoviesInfoController {
  @Autowired MoviesInfoService moviesInfoService;

  @GetMapping({"", "/"})
  public Flux<MovieInfo> getAllMovies() {
    return moviesInfoService.getAllMovies();
  }
}
