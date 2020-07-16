package com.example.internshipproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmInfoDownloader {
    @GET("??????")
    Call<List<Film>> getFiles();
}
