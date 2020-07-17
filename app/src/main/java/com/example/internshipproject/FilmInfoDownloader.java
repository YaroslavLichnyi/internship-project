package com.example.internshipproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmInfoDownloader {
    @GET("?s=war&apikey=7e6a1464")
    Call<Search> getFiles();
}
