package com.example.internshipproject;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.Search;
import com.example.internshipproject.viewmodels.FilmDetailsListener;
import com.example.internshipproject.viewmodels.FilmListListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmRepository {
    public final String BASE_URL = "http://www.omdbapi.com/";
    private Retrofit retrofit;
    private MutableLiveData<List<Film>> filmLiveData;
    private List<Film> films;
    private static FilmRepository instance;
    private FilmInfoApiService infoDownloader;
    private FilmDetails filmDetails;

    public static FilmRepository getInstance(){
        if (instance == null){
            instance = new FilmRepository();
        }
        return instance;
    }

    private FilmRepository() {
        filmLiveData = new MutableLiveData<>();
        initRetrofit();
    }

    public void getFilmLiveData(String keyWord, FilmListListener listener) {
        Call<Search> filmsData = infoDownloader.getFiles(keyWord);
        filmsData.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful() /*роверять размер бади?*/){
                    films = response.body().getSearch();
                    listener.loadFilmList(films);
                } else {
                    Log.d("Retrofit", "onResponse: response is not successful");
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d("Retrofit", "onFailure: " + t.toString());
            }
        });
    }

    public void getFilmDetailsById(String id, FilmDetailsListener listener){
        Call<FilmDetails> filmsData = infoDownloader.getFilmDetailInfo(id);
        filmsData.enqueue(new Callback<FilmDetails>() {
            @Override
            public void onResponse(Call<FilmDetails> call, Response<FilmDetails> response) {
                if(response.isSuccessful()){
                    filmDetails = response.body();
                    listener.loadFilmDetails(filmDetails);
                } else {
                    Log.d("Retrofit", "onResponse: response is not successful");
                }
            }

            @Override
            public void onFailure(Call<FilmDetails> call, Throwable t) {
                Log.d("Retrofit", "onFailure: " + t.toString());
            }
        });
    }

    private void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        infoDownloader = retrofit.create(FilmInfoApiService.class);
    }
}
