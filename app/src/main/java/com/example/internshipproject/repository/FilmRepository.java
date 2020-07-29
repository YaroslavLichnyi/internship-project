package com.example.internshipproject.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.Search;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FilmRepository {
    private static final String TAG = FilmRepository.class.getSimpleName();

    /**
     * Contains a base url that Retrofit uses for connecting to server
     */
    public final String BASE_URL = "https://www.omdbapi.com/";
    private static FilmRepository instance;
    private MutableLiveData<List<Film>> filmLiveData;
    private List<Film> films;
    private FilmDetails filmDetails;
    private Disposable subscription;

    private FilmRepository() {
        filmLiveData = new MutableLiveData<>();
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

    public Observable<Search> getFilmLiveData(String keyWord) {
        return RetrofitService
                .getInstance()
                .getFilmsInfoDownloader(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<FilmDetails> getFilmDetailsById(String id){
        return RetrofitService
                .getInstance()
                .getFilmDetailsInfoDownloader(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
