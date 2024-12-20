package com.movies.models;

public class Movie {
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String releaseYear;
    private String language;
    private String rating;

    public Movie(String title, String overview, String posterPath, String backdropPath, String releaseYear, String rating) {
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseYear = releaseYear;
        this.releaseYear = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getLanguage() {
        return language;
    }

    public String getRating() {
        return rating;
    }
}

