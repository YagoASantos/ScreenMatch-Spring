package br.com.alura.screenmatch.models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodios;
    private Double avaliacao;
    private LocalDate dataLancamento;
    public Episodio(Integer temporada, DadosEpisodio episodio) {
        this.temporada = temporada;
        this.titulo = episodio.titulo();
        this.numeroEpisodios = episodio.numeroEpisodio();
        try {
            this.avaliacao = Double.valueOf(episodio.avaliacao());
        } catch(NumberFormatException ex) {
            this.avaliacao = null;
        }
        try {
            this.dataLancamento = LocalDate.parse(episodio.dataLancamento());
        } catch(DateTimeParseException ex) {
            this.dataLancamento = null;
        }

    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodios() {
        return numeroEpisodios;
    }

    public void setNumeroEpisodios(Integer numeroEpisodios) {
        this.numeroEpisodios = numeroEpisodios;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return  "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodios=" + numeroEpisodios +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
