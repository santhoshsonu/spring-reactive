package com.spring.reactive.learn;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxMonGeneratorService {
  public Flux<String> namesFlux() {
    return Flux.fromIterable(List.of("alex", "ben", "carl", "daniel"))
        .log(); // log each and every event happening between publisher and subscriber communication
  }

  public Mono<String> nameMono() {
    return Mono.just("Luffy");
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

  public static void main(String[] args) {
    FluxMonGeneratorService service = new FluxMonGeneratorService();

    log.info("Flux names ::");
    service.namesFlux().subscribe(name -> log.info("Name: " + name));

    log.info("Mono name ::");
    service.nameMono().subscribe(name -> log.info("Name: " + name));
  }
}
