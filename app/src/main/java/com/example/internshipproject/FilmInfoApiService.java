package com.example.internshipproject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmInfoApiService {
    @GET("?s=war&apikey=7e6a1464")
    Call<Search> getFiles();
}
