package com.spring.reactive.learn;

import java.util.List;
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
  void namesFluxMap() {
    // When
    final var namesFlux = service.namesFluxMap();

    // Then
    StepVerifier.create(namesFlux).expectNext("ALEX", "BEN", "CARL", "DANIEL").verifyComplete();
  }

  @Test
  void namesFluxImmutability() {
    // When
    final var namesFlux = service.namesFluxImmutability();

    // Then
    StepVerifier.create(namesFlux).expectNext("alex", "ben", "carl", "daniel").verifyComplete();
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
    // When
    final var namesFlux = service.namesFluxFlatMap();

    // Then
    StepVerifier.create(namesFlux)
        .expectNext(
            "A", "L", "E", "X", "B", "E", "N", "C", "A", "R", "L", "D", "A", "N", "I", "E", "L")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMapDelay() {
    // When
    final var namesFlux = service.namesFluxFlatMapDelay();

    // Then
    StepVerifier.create(namesFlux).expectNextCount(17L).verifyComplete();
  }

  @Test
  void namesFluxConcatMapDelay() {
    // When
    final var namesFlux = service.namesFluxConcatMap();

    // Then
    StepVerifier.create(namesFlux)
        .expectNext(
            "A", "L", "E", "X", "B", "E", "N", "C", "A", "R", "L", "D", "A", "N", "I", "E", "L")
        .verifyComplete();
  }

  @Test
  void namesFluxTransform() {
    // When
    final var namesFlux = service.namesFluxTransform(3);

    // Then
    StepVerifier.create(namesFlux).expectNext("ALEX", "CARL", "DANIEL").verifyComplete();
  }

  @Test
  void namesFluxTransform_empty_list() {
    // When
    final var namesFlux = service.namesFluxTransform(6);

    // Then
    StepVerifier.create(namesFlux).expectNext("default").verifyComplete();
  }

  @Test
  void namesFluxTransformSwitchIfEmpty() {
    // When
    final var namesFlux = service.namesFluxTransformSwitchIfEmpty(6);

    // Then
    StepVerifier.create(namesFlux).expectNext("DEFAULT").verifyComplete();
  }

  @Test
  void exploreConcat() {
    // When
    final var namesFlux = service.exploreConcat();

    // Then
    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void exploreConcatWith() {
    // When
    final var namesFlux = service.exploreConcatWith();

    // Then
    StepVerifier.create(namesFlux).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void namesMono() {
    // When
    final var namesMono = service.namesMono();

    // Then
    StepVerifier.create(namesMono).expectNext("Luffy").verifyComplete();
  }

  @Test
  void namesMonoMap() {
    // When
    final var namesMono = service.namesMonoMap();

    // Then
    StepVerifier.create(namesMono).expectNext("LUFFY").verifyComplete();
  }

  @Test
  void namesMonoMapFilter() {
    // When
    final var namesMono = service.namesMonoMapFilter(4);

    // Then
    StepVerifier.create(namesMono).expectNext("5-LUFFY").verifyComplete();
  }

  @Test
  void namesMonoFlatMap() {
    // When
    final var namesMono = service.namesMonoFlatMap();

    // Then
    StepVerifier.create(namesMono).expectNext(List.of("L", "U", "F", "F", "Y")).verifyComplete();
  }

  @Test
  void namesMonoFlatMapMany() {
    // When
    final var namesMono = service.namesMonoFlatMapMany();

    // Then
    StepVerifier.create(namesMono).expectNext("L", "U", "F", "F", "Y").verifyComplete();
  }

  @Test
  void exploreMonoConcat() {
    // When
    final var namesFlux = service.exploreMonoConcat();

    // Then
    StepVerifier.create(namesFlux).expectNext("A", "B").verifyComplete();
  }
}
