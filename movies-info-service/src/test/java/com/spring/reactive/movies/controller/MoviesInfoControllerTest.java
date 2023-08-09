package com.spring.reactive.movies.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import com.spring.reactive.movies.exception.handlers.GlobalErrorHandler;
import com.spring.reactive.movies.model.MovieInfo;
import com.spring.reactive.movies.service.MoviesInfoService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(
    controllers = MoviesInfoController.class,
    properties = "de.flapdoodle.mongodb.embedded.version=6.0.2")
@AutoConfigureDataMongo
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
class MoviesInfoControllerTest {
  final String BASE_URL = "/v1/movies-info";

  @MockBean MoviesInfoService moviesInfoServiceMock;
  @Autowired WebTestClient webTestClient;

  @Test
  void getAllMovies() {
    // GIVEN
    List<MovieInfo> movieInfos =
        List.of(
            new MovieInfo().setTitle("Batman Begins"),
            new MovieInfo().setTitle("The Dark Knight"),
            new MovieInfo().setMovieInfoId("movie-3").setTitle("The Dark Knight Rises"));

    when(moviesInfoServiceMock.getAllMovies()).thenReturn(Flux.fromIterable(movieInfos));

    // THEN
    webTestClient.get().uri(BASE_URL).exchange().expectBodyList(MovieInfo.class).hasSize(3);
  }

  @Test
  void getMovieById() {
    // GIVEN
    final String movieInfoId = "movie-3";
    MovieInfo movieInfo =
        new MovieInfo()
            .setMovieInfoId(null)
            .setTitle("The Dark Knight Rises")
            .setYear(2012)
            .setCast(List.of("Christian Bale", "Tom Hardy", "Anna Hathaway", "Logan Lerman"))
            .setReleaseDate(LocalDate.parse("2012-07-20"));
    when(moviesInfoServiceMock.getMovieById(any(String.class)))
        .thenReturn(Mono.just(movieInfo.setMovieInfoId(movieInfoId)));

    // THEN
    webTestClient
        .get()
        .uri(BASE_URL + "/{id}", movieInfoId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo body = result.getResponseBody();
              assertNotNull(body);
              assertEquals(movieInfoId, body.getMovieInfoId());
              assertEquals(movieInfo.getTitle(), body.getTitle());
            });
  }

  @Test
  void addNewMovie_validations() {
    // GIVEN
    MovieInfo movieInfo = new MovieInfo().setCast(List.of(""));

    // THEN
    webTestClient
        .post()
        .uri(BASE_URL)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(GlobalErrorHandler.ErrorResponse.class)
        .consumeWith(
            result -> {
              GlobalErrorHandler.ErrorResponse body = result.getResponseBody();
              assertNotNull(body);
              assertEquals("Invalid data", body.getMessage());
              assertEquals(HttpStatus.BAD_REQUEST, body.getStatus());
              assertEquals("Title is required", body.getErrors().get("title"));
              assertEquals("Year is required", body.getErrors().get("year"));
              assertEquals("Cast is required", body.getErrors().get("cast[0]"));
            });
  }

  @Test
  void addNewMovie() {
    // GIVEN
    MovieInfo movieInfo =
        new MovieInfo()
            .setTitle("Batman Begins")
            .setYear(2005)
            .setCast(List.of("Christian Bale", "Michael Caine", "Gary Oldman"))
            .setReleaseDate(LocalDate.parse("2005-05-14"));

    when(moviesInfoServiceMock.createMovie(any(MovieInfo.class)))
        .thenReturn(Mono.just(movieInfo.setMovieInfoId("mockId").setTitle("Interstellar")));
    // THEN
    webTestClient
        .post()
        .uri(BASE_URL)
        .bodyValue(movieInfo.setTitle("Interstellar"))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo body = result.getResponseBody();
              assertNotNull(body);
              assertEquals("mockId", body.getMovieInfoId());
              assertEquals("Interstellar", body.getTitle());
            });
  }

  @Test
  void updateMovie() {
    // GIVEN
    final String movieInfoId = "movie-3";
    MovieInfo movieInfo =
        new MovieInfo()
            .setMovieInfoId(null)
            .setTitle("The Dark Knight Rises")
            .setYear(2012)
            .setCast(List.of("Christian Bale", "Tom Hardy", "Anna Hathaway", "Logan Lerman"))
            .setReleaseDate(LocalDate.parse("2012-07-20"));
    when(moviesInfoServiceMock.updateMovie(isA(String.class), isA(MovieInfo.class)))
        .thenReturn(Mono.just(movieInfo.setMovieInfoId(movieInfoId)));

    // THEN
    webTestClient
        .put()
        .uri(BASE_URL + "/{id}", movieInfoId)
        .bodyValue(movieInfo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(MovieInfo.class)
        .consumeWith(
            result -> {
              MovieInfo body = result.getResponseBody();
              assertNotNull(body);
              assertEquals(movieInfoId, body.getMovieInfoId());
              assertEquals(movieInfo.getTitle(), body.getTitle());
            });
  }

  @Test
  void deleteMovie() {
    // GIVEN
    final String movieInfoId = "movie-3";
    when(moviesInfoServiceMock.deleteMovie(isA(String.class))).thenReturn(Mono.empty());

    // THEN
    webTestClient
        .delete()
        .uri(BASE_URL + "/{id}", movieInfoId)
        .exchange()
        .expectStatus()
        .isNoContent();
  }
}
