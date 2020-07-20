package com.example.internshipproject;

import com.example.internshipproject.entities.FilmDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmInfoApiService {
    @GET("?s=war&apikey=7e6a1464")
    Call<Search> getFiles();

    @GET("?apikey=6ee19dd4&plot=full")
    Call<FilmDetails> getFilmDetailInfo(@Query("i") String imdbId);
}
