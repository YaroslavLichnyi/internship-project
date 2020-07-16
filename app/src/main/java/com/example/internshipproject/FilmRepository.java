package com.example.internshipproject;

import androidx.lifecycle.LiveData;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmRepository {
    public final String BASE_URL = "http://www.omdbapi.com/?i=tt3896198&apikey=7e6a1464";
    private Retrofit retrofit;
    private LiveData<List<Film>> filmList;

    public FilmRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }
}
