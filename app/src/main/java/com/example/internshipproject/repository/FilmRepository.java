package com.example.internshipproject.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.BuildConfig;
import com.example.internshipproject.FilmInfoApiService;
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
    private static final String TAG = FilmRepository.class.getSimpleName();

    /**
     * Contains a base url that Retrofit uses for connecting to server
     */
    public final String BASE_URL = "http://www.omdbapi.com/";
    private static FilmRepository instance;
    private Retrofit retrofit;
    private MutableLiveData<List<Film>> filmLiveData;
    private List<Film> films;
    private FilmInfoApiService infoDownloader;
    private FilmDetails filmDetails;

    private FilmRepository() {
        filmLiveData = new MutableLiveData<>();
        initRetrofit();
    }

    /**
     * This method is using for implementing Observer pattern.
     *
     * @return an instance of {@link FilmRepository}. It is only one object of {@link FilmRepository}
     * class.
     */
    public static FilmRepository getInstance(){
        if (instance == null){
            instance = new FilmRepository();
        }
        return instance;
    }

    /**
     * Gets data about films from server using key word which searching is based on. If response
     * returns list of films then these films put into LiveData at ModelView that serves for
     * delivering films to fragment. If number of films is less then 0 or there are some troubles
     * with getting information from server then user informs about it.
     *
     * @param keyWord is a word or a set of words which are used for finding films.
     * @param listener is used for setting values into ModelView depending on get's it correct
     *                 values from server or not.
     */
    public void getFilmLiveData(String keyWord, FilmListListener listener) {
        Call<Search> filmsData = infoDownloader.getFiles(keyWord);
        filmsData.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful()){
                    films = response.body().getSearch();
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse:" + films );
                    if (films.size() > 0){
                        listener.loadFilmList(films);
                    } else {
                        listener.notifyAboutNotSuccessfulResponse("No films were found with this key word");
                    }
                } else {
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: response is not successful");
                    listener.notifyAboutNotSuccessfulResponse("Troubles with server connection");
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                if (BuildConfig.DEBUG) Log.e(TAG, "onFailure: ", t);
                listener.notifyAboutNotSuccessfulResponse("Error:" + t.toString());
            }
        });
    }

    /**
     * Gets data about film details from server using film's unique id. If response returns film
     * details then this detail information about film puts into LiveData at ModelView that serves
     * for delivering films to fragment.If response is not correct that user informs about it.
     *
     * @param id is a unique id of film. Film is searched by it.
     * @param listener is used for setting values into ModelView depending on get's it correct
     *                 values from server or not.
     */
    public void getFilmDetailsById(String id, FilmDetailsListener listener){
        Call<FilmDetails> filmsData = infoDownloader.getFilmDetailInfo(id);
        filmsData.enqueue(new Callback<FilmDetails>() {
            @Override
            public void onResponse(Call<FilmDetails> call, Response<FilmDetails> response) {
                if(response.isSuccessful()){
                    filmDetails = response.body();
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: " + filmDetails);
                    listener.loadFilmDetails(filmDetails);
                } else {
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponse: response is not successful");
                    listener.notifyAboutNotSuccessfulResponse("Troubles with server connection");
                }
            }

            @Override
            public void onFailure(Call<FilmDetails> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.notifyAboutNotSuccessfulResponse("" +  t.toString());
            }
        });
    }

    /**
     * Initializes Retrofit object and sets API to it
     */
    private void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        infoDownloader = retrofit.create(FilmInfoApiService.class);
    }
}
