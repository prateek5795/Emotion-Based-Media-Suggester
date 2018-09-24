package com.example.prateek.visionapitest.Rest;

import com.example.prateek.visionapitest.Model.MovieResponse;
import com.example.prateek.visionapitest.Model.MusicResponse;
import com.example.prateek.visionapitest.Model.Tracks;
import com.example.prateek.visionapitest.Model.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MovieResponse> getMovieAccGenre(@Query("api_key") String apiKey, @Query("with_genres") String genres);

    @GET("discover/tv")
    Call<TvResponse> getTvAccGenre(@Query("api_key") String apiKey, @Query("with_genres") String genres);

    @GET("?method=tag.gettoptracks")
    Call<MusicResponse> getTracks(@Query("tag") String tag, @Query("api_key") String apiKey, @Query("format") String format);

}



