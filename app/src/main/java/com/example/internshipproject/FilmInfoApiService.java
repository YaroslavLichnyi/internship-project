package com.example.internshipproject;

import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 */
public interface FilmInfoApiService {
    /**
     *
     * @param keyWord
     * @return
     */
    @GET("?apikey=7e6a1464")
    Call<Search> getFiles(@Query("s") String keyWord);

    /**
     *
     * @param imdbId
     * @return
     */
    @GET("?apikey=7e6a1464&plot=full")
    Call<FilmDetails> getFilmDetailInfo(@Query("i") String imdbId);
}
