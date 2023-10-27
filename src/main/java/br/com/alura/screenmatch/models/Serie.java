package br.com.alura.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Serie(@JsonAlias("Title") String title,
                    @JsonAlias("imdsRating") String rating,
                    @JsonAlias("totalSeasons")Integer totalTemps) {

}
