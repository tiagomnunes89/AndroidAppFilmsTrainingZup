package br.com.estagio.oletrainning.zup.otmovies.server.response;

import java.util.List;

public class FilmsResults {

    private final Integer page;
    private final Integer totalMovies;
    private final Integer totalPages;

    private final List<FilmResponse> results;

    public FilmsResults(Integer page, Integer totalMovies, Integer totalPages, List<FilmResponse> results) {
        this.page = page;
        this.totalMovies = totalMovies;
        this.totalPages = totalPages;
        this.results = results;
    }

    public List<FilmResponse> getResults() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal_results() {
        return totalMovies;
    }

    public Integer getTotal_pages() {
        return totalPages;
    }




}
