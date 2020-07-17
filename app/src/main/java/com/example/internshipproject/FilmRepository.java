package com.example.internshipproject;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmRepository {
    public final String BASE_URL = "http://www.omdbapi.com/";
    private Retrofit retrofit;
    private LiveData<List<Film>> filmList;

    public FilmRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FilmInfoDownloader infoDownloader = retrofit.create(FilmInfoDownloader.class);
        Call<Search> filmsData = infoDownloader.getFiles();
        filmsData.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful()){
                    List<Film> films = response.body().getSearch();
                    Log.d("===============", "onResponse: " + films.toString());
                    filmList = new MutableLiveData<List<Film>>(films);
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d("Retrofit", "onFailure: " + t.toString());
            }
        });
    }

    public LiveData<List<Film>> getFilmList() {
        return filmList;
    }

    public void setFilmList(LiveData<List<Film>> filmList) {
        this.filmList = filmList;
    }
}
