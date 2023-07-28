package com.spring.reactive.movies.controller;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxMonoController {
  @GetMapping("/flux")
  public Flux<Integer> flux() {
    return Flux.just(1, 2, 3, 4, 5);
  }

  @GetMapping("/mono")
  public Mono<String> helloWorldMono() {
    return Mono.just("Hello World!");
  }

  // Server Sent Events (SSE)
  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Long> stream() {
    return Flux.interval(Duration.ofSeconds(1));
  }
}
