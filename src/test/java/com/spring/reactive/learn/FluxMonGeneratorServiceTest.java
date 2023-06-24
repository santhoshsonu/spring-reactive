package com.spring.reactive.learn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxMonGeneratorServiceTest {

  FluxMonGeneratorService service = new FluxMonGeneratorService();

  @Test
  void namesFlux() {
    // When
    final var namesFlux = service.namesFlux();

    // Then
    StepVerifier.create(namesFlux)
        // .expectNext("alex", "ben", "carl", "daniel") // Validate each and every element
        // .expectNextCount(4L) // Validate total count
        .expectNext("alex")
        .expectNextCount(3L)
        .verifyComplete();
  }

  @Test
  void namesFluxMapFilter() {
    // When
    final var namesFlux = service.namesFluxMapFilter(3);

    // Then
    StepVerifier.create(namesFlux)
        .expectNext("4-ALEX", "4-CARL", "6-DANIEL") // Validate each and every element
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMap() {
    final var namesFlux = service.namesFluxFlatMap();

    StepVerifier.create(namesFlux)
        .expectNext(
            "A", "L", "E", "X", "B", "E", "N", "C", "A", "R", "L", "D", "A", "N", "I", "E", "L")
        .verifyComplete();
  }
}
