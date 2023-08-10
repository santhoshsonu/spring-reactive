package com.spring.reactive.movies.controller;

import static java.util.Objects.nonNull;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.service.MoviesInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies-info")
public class MoviesInfoController {
  @Autowired MoviesInfoService moviesInfoService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addNewMovie(@RequestBody @Valid MovieInfo movieInfo) {
    return moviesInfoService.createMovie(movieInfo);
  }

  @GetMapping({"", "/"})
  public Flux<MovieInfo> getAllMovies(
      @RequestParam(value = "year", required = false) Integer year,
      @RequestParam(value = "name", required = false) String name) {
    boolean filterByYear = nonNull(year);
    boolean filterByName = nonNull(name);
    if (filterByYear && filterByName) {
      return moviesInfoService.getMoviesByNameAndYear(name, year);
    }
    if (filterByName) {
      return moviesInfoService.getMoviesByName(name);
    }
    if (filterByYear) {
      return moviesInfoService.getMoviesByYear(year);
    }
    return moviesInfoService.getAllMovies();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<MovieInfo>> getMovieById(@PathVariable("id") String id) {
    return moviesInfoService
        .getMovieById(id)
        .map(ResponseEntity.ok()::body)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<MovieInfo>> updateMovie(
      @PathVariable("id") String id, @RequestBody @Valid MovieInfo movieInfo) {
    return moviesInfoService
        .updateMovie(id, movieInfo)
        .map(ResponseEntity.ok()::body)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovie(@PathVariable("id") String id) {
    return moviesInfoService.deleteMovie(id);
  }
}
