package com.spring.reactive.learn;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxMonGeneratorService {
  public Flux<String> namesFlux() {
    return Flux.fromIterable(List.of("alex", "ben", "carl", "daniel"))
        .log(); // log each and every event happening between publisher and subscriber communication
  }

  public Mono<String> namesMono() {
    return Mono.just("Luffy");
  }

  public Flux<String> namesFluxMap() {
    return namesFlux().map(String::toUpperCase).log();
  }

  public Flux<String> namesFluxImmutability() {
    final var namesFlux = Flux.fromIterable(List.of("alex", "ben", "carl", "daniel"));
    namesFlux.map(String::toUpperCase);
    return namesFlux;
  }

  public Flux<String> namesFluxMapFilter(int strLength) {
    return namesFlux()
        .filter(s -> s.length() > strLength)
        .map(String::toUpperCase)
        .map(s -> s.length() + "-" + s)
        .log();
  }

  public Flux<String> namesFluxFlatMap() {
    return namesFlux().flatMap(s -> Flux.fromArray(s.toUpperCase().split(""))).log();
  }

  public Flux<String> namesFluxFlatMapDelay() {
    return namesFlux()
        .flatMap(
            s ->
                Flux.fromArray(s.toUpperCase().split(""))
                    .delayElements(Duration.ofMillis(new Random().nextInt(1000))))
        .log();
  }

  public Flux<String> namesFluxConcatMap() {
    return namesFlux()
        .concatMap(
            s ->
                Flux.fromArray(s.toUpperCase().split(""))
                    .delayElements(Duration.ofMillis(new Random().nextInt(1000))))
        .log();
  }

  public Flux<String> namesFluxTransform(int strLength) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.filter(s -> s.length() > strLength).map(String::toUpperCase);
    return namesFlux().transform(filterMap).defaultIfEmpty("default").log();
  }

  public Flux<String> namesFluxTransformSwitchIfEmpty(int strLength) {
    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.filter(s -> s.length() > strLength).map(String::toUpperCase);

    Flux<String> defaultFlux = Flux.just("default").transform(filterMap);

    return namesFlux().transform(filterMap).switchIfEmpty(defaultFlux).log();
  }

  public Flux<String> exploreConcat() {
    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");
    return Flux.concat(abcFlux, defFlux);
  }

  public Flux<String> exploreConcatWith() {
    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");
    return abcFlux.concatWith(defFlux);
  }

  public Mono<String> namesMonoMap() {
    return namesMono().map(String::toUpperCase).log();
  }

  public Mono<String> namesMonoMapFilter(int strLength) {
    return namesMono()
        .filter(s -> s.length() > strLength)
        .map(String::toUpperCase)
        .map(s -> s.length() + "-" + s)
        .log();
  }

  public Mono<List<String>> namesMonoFlatMap() {
    return namesMono().flatMap(s -> Mono.just(List.of(s.toUpperCase().split("")))).log();
  }

  public Flux<String> namesMonoFlatMapMany() {
    return namesMono().flatMapMany(s -> Flux.fromArray(s.toUpperCase().split(""))).log();
  }

  public Flux<String> exploreMonoConcat() {
    Mono<String> aMono = Mono.just("A");
    Mono<String> bMono = Mono.just("B");
    return aMono.concatWith(bMono).log();
  }

  public static void main(String[] args) {
    FluxMonGeneratorService service = new FluxMonGeneratorService();

    log.info("Flux names ::");
    service.namesFlux().subscribe(name -> log.info("Name: " + name));

    log.info("Flux flatmap names ::");
    service.namesFluxFlatMap().subscribe(name -> log.info("Name: " + name));

    log.info("Mono name ::");
    service.namesMono().subscribe(name -> log.info("Name: " + name));
  }
}
