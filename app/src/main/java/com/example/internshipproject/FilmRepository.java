package com.example.internshipproject;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//сделать синглтоном
public class FilmRepository {
    public final String BASE_URL = "http://www.omdbapi.com/";
    private Retrofit retrofit;
    private MutableLiveData<List<Film>> filmLiveData;
    private List<Film> films;

    public FilmRepository() {
        filmLiveData = new MutableLiveData<>();
        //films = new ArrayList<>();
        //filmLiveData.setValue(films);

    }

    public LiveData<List<Film>> getFilmLiveData() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FilmInfoApiService infoDownloader = retrofit.create(FilmInfoApiService.class);
        Call<Search> filmsData = infoDownloader.getFiles();
        filmsData.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful()){
                    films = response.body().getSearch();
                    Log.d("Retrofit", "onResponse: " + films.toString());
                    // setValue - передача значения из главного потока, postValue - из другого
                    //filmLiveData.postValue(films);
                    filmLiveData.setValue(films);
                } else {
                    Log.d("Retrofit", "onResponse: response is not successful");
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d("Retrofit", "onFailure: " + t.toString());
            }
        });
        return filmLiveData;
    }
}
