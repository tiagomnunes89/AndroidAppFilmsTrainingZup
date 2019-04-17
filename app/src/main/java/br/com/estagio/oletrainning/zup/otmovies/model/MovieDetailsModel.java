package br.com.estagio.oletrainning.zup.otmovies.model;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsModel {

    private int id;
    private String posterId;
    private String bannerId;
    private float voteAverage;
    private int voteCount;
    private String title;
    private int year;
    private List<String> genreNames = new ArrayList<>();
    private String runtime;
    private String overview;
    private boolean favorit;
    private float price;
    private boolean acquired;
    private List<String> directors = new ArrayList<>();
    private List<String> writers = new ArrayList<>();
    private List<String> countries = new ArrayList<>();

    public MovieDetailsModel() {
    }

    public MovieDetailsModel(int id, String posterId, String bannerId, float voteAverage, String title, int year, List<String> genreNames,
                             String runtime, String overview, List<String> directors, List<String> writers, List<String> countries) {
        this.id = id;
        this.posterId = posterId;
        this.bannerId = bannerId;
        this.voteAverage = voteAverage;
        this.title = title;
        this.year = year;
        this.genreNames = genreNames;
        this.runtime = runtime;
        this.overview = overview;
        this.directors = directors;
        this.writers = writers;
        this.countries = countries;
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

    public int getVoteCount() {
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

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getCountries() {
        return countries;
    }


}
