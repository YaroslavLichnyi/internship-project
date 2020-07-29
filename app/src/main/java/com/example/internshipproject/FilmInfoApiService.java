package com.example.internshipproject;

import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.Search;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Serves for using its method for getting know what API to use for making responses on server.
 */
public interface FilmInfoApiService {
    /**
     * Contains API for getting getting films from server using key word.
     *
     * @param keyWord is a key word depending on which search of films is going on.
     * @return POJO which was created based on JSON document.
     */
    @GET("?apikey=7e6a1464")
    Observable<Search> getFiles(@Query("s") String keyWord);

    /**
     * Contains API for getting getting detail information about film from server using its
     * unique id.
     *
     * @param imdbId is a unique id of films depending on which search of films is going on.
     * @return POJO which was created based on JSON document.
     */
    @GET("?apikey=7e6a1464&plot=full")
    Observable<FilmDetails> getFilmDetailInfo(@Query("i") String imdbId);
}
