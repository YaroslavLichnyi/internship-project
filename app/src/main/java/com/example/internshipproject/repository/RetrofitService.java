package com.example.internshipproject.repository;

import com.example.internshipproject.services.FilmInfoApiService;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.Search;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    public static final String BASE_URL = "https://www.omdbapi.com/";
    private static RetrofitService retrofitService;
    private FilmInfoApiService infoDownloader;

    public static RetrofitService getInstance(){
        if(retrofitService == null){
            retrofitService = new RetrofitService();
            retrofitService.retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            retrofitService.infoDownloader = retrofitService.retrofit.create(FilmInfoApiService.class);
        }
        return retrofitService;
    }

    /**
     * Initializes Retrofit object and sets API to it
     */
    private RetrofitService(){
    }

    public Observable<FilmDetails> getFilmDetailsInfoDownloader(String id){
        return infoDownloader.getFilmDetailInfo(id);
    }

    public Observable<Search> getFilmsInfoDownloader(String keyWord){
        return infoDownloader.getFiles(keyWord);
    }

}
