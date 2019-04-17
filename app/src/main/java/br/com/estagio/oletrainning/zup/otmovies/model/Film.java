package br.com.estagio.oletrainning.zup.otmovies.model;

import java.util.ArrayList;
import java.util.List;

public class Film {

    private final int id;
    private final String posterId;
    private final String bannerId;
    private final float voteAverage;
    private final float voteCount;
    private final String title;
    private final int year;
    private List<String> genreNames = new ArrayList<>();
    private final String runtime;
    private final String overview;
    private final boolean favorit;
    private final float price;
    private final boolean acquired;


    public Film(int id, String posterId, String bannerId, float voteAverage, float voteCount, String title, int year,
                List<String> genreNames,String runtime, String overview, boolean favorit, float price, boolean acquired) {
        this.id = id;
        this.posterId = posterId;
        this.bannerId = bannerId;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.title = title;
        this.year = year;
        this.genreNames = genreNames;
        this.runtime = runtime;
        this.overview = overview;
        this.favorit = favorit;
        this.price = price;
        this.acquired = acquired;
    }

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
