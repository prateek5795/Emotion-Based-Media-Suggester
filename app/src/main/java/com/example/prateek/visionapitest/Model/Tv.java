package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Tv {

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("id")
    private Integer id;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("origin_country")
    private List<String> originCountry = new ArrayList<String>();

    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("vote_count")
    private Double voteCount;

    @SerializedName("name")
    private String name;

    @SerializedName("original_name")
    private String originalName;

    public Tv(String posterPath, Double popularity, Integer id, String backdropPath, Double voteAverage, String overview, String firstAirDate, List<String> originCountry, List<Integer> genreIds, String originalLanguage, Double voteCount, String name, String originalName) {
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.id = id;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.firstAirDate = firstAirDate;
        this.originCountry = originCountry;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.voteCount = voteCount;
        this.name = name;
        this.originalName = originalName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getId() {
        return id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public Double getVoteCount() {
        return voteCount;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }
}
