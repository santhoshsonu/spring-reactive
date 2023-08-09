package com.spring.reactive.movies.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.repository.MovieInfoRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "de.flapdoodle.mongodb.embedded.version=6.0.2")
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoIntegrationTest {

  final String BASE_URL = "/v1/movies-info";
  @Autowired WebTestClient webTestClient;
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
  void addNewMovie() {
    // GIVEN
    MovieInfo movieInfo =
        new MovieInfo()
            .setMovieInfoId("movie-1")
            .setTitle("Batman Begins")
            .setYear(2005)
            .setCast(List.of("Christian Bale", "Michael Caine", "Gary Oldman"))
            .setReleaseDate(LocalDate.parse("2005-05-14"));

    // WHEN
    webTestClient
        .post()
        .uri(BASE_URL)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo createdMovieInfo = result.getResponseBody();
              assertNotNull(createdMovieInfo);
              assertEquals(movieInfo, createdMovieInfo);
            });
    MovieInfo createdMovieInfo = movieInfoRepository.findById("movie-1").block();

    // THEN
    assertNotNull(createdMovieInfo);
    assertEquals(movieInfo, createdMovieInfo);
  }

  @Test
  void getAllMovies() {
    // WHEN
    webTestClient.get().uri(BASE_URL).exchange().expectBodyList(MovieInfo.class).hasSize(3);
  }

  @Test
  void getMovieById() {
    // WHEN
    final String movieId = "movie-3";
    webTestClient
        .get()
        .uri(BASE_URL + "/{id}", movieId)
        .exchange()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo movieInfo = result.getResponseBody();
              assertNotNull(movieInfo);
              assertEquals(movieId, movieInfo.getMovieInfoId());
              assertEquals("The Dark Knight Rises", movieInfo.getTitle());
              assertEquals(2012, movieInfo.getYear());
              assertEquals(
                  List.of("Christian Bale", "Tom Hardy", "Anna Hathaway", "Logan Lerman"),
                  movieInfo.getCast());
              assertEquals(LocalDate.parse("2012-07-20"), movieInfo.getReleaseDate());
            });
  }

  @Test
  void updateMovie() {
    // WHEN
    final String movieId = "movie-3";
    MovieInfo movieInfo =
        new MovieInfo()
            .setMovieInfoId("movie-3")
            .setTitle("Interstellar")
            .setYear(2014)
            .setCast(List.of("Matthew McConaughey", "Anne Hathaway"))
            .setReleaseDate(LocalDate.parse("2014-11-11"));

    // THEN
    webTestClient
        .put()
        .uri(BASE_URL + "/{id}", movieId)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo updatedMovieInfo = result.getResponseBody();
              assertNotNull(updatedMovieInfo);
              assertEquals(movieId, updatedMovieInfo.getMovieInfoId());
              assertEquals("Interstellar", updatedMovieInfo.getTitle());
              assertEquals(2014, updatedMovieInfo.getYear());
              assertEquals(
                  List.of("Matthew McConaughey", "Anne Hathaway"), updatedMovieInfo.getCast());
              assertEquals(LocalDate.parse("2014-11-11"), updatedMovieInfo.getReleaseDate());
            });
  }

  @Test
  void updateMovie_notFound() {
    // WHEN
    final String movieId = "movie-not-found";
    MovieInfo movieInfo =
        new MovieInfo()
            .setMovieInfoId("movie-3")
            .setTitle("Interstellar")
            .setYear(2014)
            .setCast(List.of("Matthew McConaughey", "Anne Hathaway"))
            .setReleaseDate(LocalDate.parse("2014-11-11"));

    // THEN
    webTestClient
        .put()
        .uri(BASE_URL + "/{id}", movieId)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void deleteMovie() {
    // WHEN
    final String movieId = "movie-3";
    webTestClient.delete().uri(BASE_URL + "/{id}", movieId).exchange().expectStatus().isNoContent();
  }
}
