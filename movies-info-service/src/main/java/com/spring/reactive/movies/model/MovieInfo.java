package com.spring.reactive.movies.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Document(collection = "movies")
public class MovieInfo {
  @Id private String movieInfoId;
  private String title;
  private Integer year;
  private List<String> cast;
  private LocalDate releaseDate;
}
