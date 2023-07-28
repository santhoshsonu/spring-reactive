package com.spring.reactive.movies.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = FluxMonoController.class)
@AutoConfigureWebTestClient
class FluxMonoControllerTest {

  @Autowired WebTestClient webTestClient;

  @Test
  void flux() {
    // WHEN
    Flux<Integer> response =
        webTestClient
            .get()
            .uri("/flux")
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Integer.class)
            .getResponseBody();

    // THEN
    StepVerifier.create(response).expectNext(1, 2, 3, 4, 5).verifyComplete();
  }

  @Test
  void flux_approach_1() {
    // WHEN
    webTestClient
        .get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Integer.class)
        .consumeWith(
            result -> {
              List<Integer> body = result.getResponseBody();
              assert Objects.requireNonNull(body).containsAll(List.of(1, 2, 3, 4, 5));
            });
  }

  @Test
  void helloWorldMono() {
    webTestClient
        .get()
        .uri("/mono")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .consumeWith(result -> assertEquals("Hello World!", result.getResponseBody()));
  }

  @Test
  void stream() {
    // WHEN
    Flux<Long> body =
        webTestClient
            .get()
            .uri("/stream")
            .exchange()
            .expectStatus()
            .isOk()
            .returnResult(Long.class)
            .getResponseBody();

    // THEN
    StepVerifier.create(body).expectNext(0L, 1L, 2L, 3L, 4L).thenCancel().verify();
  }
}
