package com.spring.reactive.movies.service;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {
  @Autowired MovieInfoRepository movieInfoRepository;

  public Flux<MovieInfo> getAllMovies() {
    return movieInfoRepository.findAll();
  }

  public Mono<MovieInfo> createMovie(MovieInfo movieInfo) {
    return movieInfoRepository.save(movieInfo);
  }

  public Mono<MovieInfo> getMovieById(String id) {
    return movieInfoRepository.findById(id);
  }

  public Mono<MovieInfo> updateMovie(String id, MovieInfo movieInfo) {
    return movieInfoRepository
        .findById(id)
        .flatMap(
            movie -> {
              movie.setTitle(movieInfo.getTitle());
              movie.setYear(movieInfo.getYear());
              movie.setCast(movieInfo.getCast());
              movie.setReleaseDate(movieInfo.getReleaseDate());
              return movieInfoRepository.save(movie);
            });
  }

  public Mono<Void> deleteMovie(String id) {
    return movieInfoRepository.deleteById(id);
  }
}
