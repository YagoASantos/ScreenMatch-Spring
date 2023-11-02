package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("imdbRating") String rating,
                         @JsonAlias("totalSeasons")Integer totalTemporadas,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("Genre")String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster) {

}
