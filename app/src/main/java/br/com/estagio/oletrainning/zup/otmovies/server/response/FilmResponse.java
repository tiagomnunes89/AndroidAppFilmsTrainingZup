package br.com.estagio.oletrainning.zup.otmovies.server.response;

import java.util.ArrayList;
import java.util.List;

public class FilmResponse {

    private int id;
    private String posterId;
    private String bannerId;
    private float voteAverage;
    private float voteCount;
    private String title;
    private int year;
    private List<String> genreNames = new ArrayList<>();
    private String runtime;
    private String overview;
    private boolean favorit;
    private float price;
    private boolean acquired;

    public int getId() {
        return id;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getBannerId() {
        return bannerId;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isFavorit() {
        return favorit;
    }

    public float getPrice() {
        return price;
    }

    public boolean isAcquired() {
        return acquired;
    }
}
