package com.spring.reactive.movies.repository;

import com.spring.reactive.movies.model.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
  Flux<MovieInfo> findByYear(Integer year);

  Flux<MovieInfo> findByTitle(String title);

  Flux<MovieInfo> findByTitleAndYear(String title, Integer year);
}
