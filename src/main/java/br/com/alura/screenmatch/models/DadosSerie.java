package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String title,
                         @JsonAlias("imdbRating") String rating,
                         @JsonAlias("totalSeasons")Integer totalTemps) {

}
