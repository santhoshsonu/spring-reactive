package com.spring.reactive.movies.service;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MoviesInfoService {
  @Autowired MovieInfoRepository movieInfoRepository;

  public Flux<MovieInfo> getAllMovies() {
    return movieInfoRepository.findAll();
  }
}
