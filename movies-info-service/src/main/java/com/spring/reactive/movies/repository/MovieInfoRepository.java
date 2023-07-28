package com.spring.reactive.movies.repository;

import com.spring.reactive.movies.model.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {}
