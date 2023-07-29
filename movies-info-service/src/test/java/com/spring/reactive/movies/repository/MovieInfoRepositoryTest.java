package com.spring.reactive.movies.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.spring.reactive.movies.model.MovieInfo;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@AutoConfigureDataMongo
@SpringBootTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
@ActiveProfiles("test")
class MovieInfoRepositoryTest {
  @Autowired MovieInfoRepository movieInfoRepository;

  @BeforeEach
  void setUp() {
    List<MovieInfo> movieInfos =
        List.of(
            new MovieInfo()
                .setTitle("Batman Begins")
                .setYear(2005)
                .setCast(List.of("Christian Bale", "Michael Caine", "Gary Oldman"))
                .setReleaseDate(LocalDate.parse("2005-05-14")),
            new MovieInfo()
                .setTitle("The Dark Knight")
                .setYear(2008)
                .setCast(List.of("Christian Bale", "Heath Ledger", "Aaron Eckhart"))
                .setReleaseDate(LocalDate.parse("2008-07-18")),
            new MovieInfo()
                .setMovieInfoId("movie-3")
                .setTitle("The Dark Knight Rises")
                .setYear(2012)
                .setCast(List.of("Christian Bale", "Tom Hardy", "Anna Hathaway", "Logan Lerman"))
                .setReleaseDate(LocalDate.parse("2012-07-20")));
    movieInfoRepository.saveAll(movieInfos).blockLast();
  }

  @AfterEach
  void tearDown() {
    movieInfoRepository.deleteAll().block();
  }

  @Test
  void testFindAll() {
    final Flux<MovieInfo> movieInfos = movieInfoRepository.findAll();
    StepVerifier.create(movieInfos)
        .assertNext(movieInfo -> assertEquals("Batman Begins", movieInfo.getTitle()))
        .assertNext(movieInfo -> assertEquals("The Dark Knight", movieInfo.getTitle()))
        .assertNext(movieInfo -> assertEquals("The Dark Knight Rises", movieInfo.getTitle()))
        .verifyComplete();
  }

  @Test
  void testFindById() {
    final Mono<MovieInfo> movieInfo = movieInfoRepository.findById("movie-3");
    StepVerifier.create(movieInfo)
        .assertNext(movie -> assertEquals("The Dark Knight Rises", movie.getTitle()))
        .verifyComplete();
  }

  @Test
  void testSaveMovieInfo() {
    final Mono<MovieInfo> movieInfo =
        movieInfoRepository.save(new MovieInfo().setTitle("Interstellar").setYear(2014));
    StepVerifier.create(movieInfo)
        .assertNext(
            movie -> {
              assertNotNull(movie.getMovieInfoId());
              assertEquals("Interstellar", movie.getTitle());
            })
        .verifyComplete();
  }

  @Test
  void testUpdateMovieInfo() {
    final MovieInfo movieInfo = movieInfoRepository.findById("movie-3").block();

    assert movieInfo != null;
    movieInfo.setCast(List.of("Christian Bale", "Michael Caine"));

    Mono<MovieInfo> updatedMovieInfo = movieInfoRepository.save(movieInfo);

    StepVerifier.create(updatedMovieInfo)
        .assertNext(
            movie -> {
              assertEquals("The Dark Knight Rises", movie.getTitle());
              assertEquals(List.of("Christian Bale", "Michael Caine"), movie.getCast());
            })
        .verifyComplete();
  }

  @Test
  void deleteUpdateMovieInfo() {
    movieInfoRepository.deleteById("movie-3").block();

    final Mono<MovieInfo> movieInfo = movieInfoRepository.findById("movie-3");

    StepVerifier.create(movieInfo).expectNextCount(0).verifyComplete();
  }
}
