package com.spring.reactive.movies.controller;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.service.MoviesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies-info")
public class MoviesInfoController {
  @Autowired MoviesInfoService moviesInfoService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addNewMovie(@RequestBody MovieInfo movieInfo) {
    return moviesInfoService.createMovie(movieInfo);
  }

  @GetMapping({"", "/"})
  public Flux<MovieInfo> getAllMovies() {
    return moviesInfoService.getAllMovies();
  }

  @GetMapping("/{id}")
  public Mono<MovieInfo> getMovieById(@PathVariable("id") String id) {
    return moviesInfoService.getMovieById(id);
  }

  @PutMapping("/{id}")
  public Mono<MovieInfo> updateMovie(@PathVariable("id") String id, @RequestBody MovieInfo movieInfo) {
    return moviesInfoService.updateMovie(id, movieInfo);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovie(@PathVariable("id") String id) {
    return moviesInfoService.deleteMovie(id);
  }
}
