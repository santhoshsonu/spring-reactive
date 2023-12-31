package com.spring.reactive.movies.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

  @NotBlank(message = "Title is required")
  private String title;

  @NotNull(message = "Year is required")
  @Positive(message = "Year must be positive number")
  private Integer year;

  private List<@NotBlank(message = "Cast is required") String> cast;

  private LocalDate releaseDate;
}
