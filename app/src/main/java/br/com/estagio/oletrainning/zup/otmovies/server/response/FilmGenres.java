package br.com.estagio.oletrainning.zup.otmovies.server.response;

import java.util.List;

public class FilmGenres {

    private final List<GenresResponse> genres;

    public FilmGenres(List<GenresResponse> genres) {
        this.genres = genres;
    }

    public List<GenresResponse> getGenres() {
        return genres;
    }
}

