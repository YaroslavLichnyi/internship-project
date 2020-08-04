package com.example.internshipproject.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmInformationHolder;
import com.example.internshipproject.repository.FilmRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FilmListViewModel extends AndroidViewModel {
    private static String TAG = FilmListViewModel.class.getSimpleName();
    private MutableLiveData<List<FilmInformationHolder>> filmLiveData;
    private MutableLiveData<String> filmLiveDataExceptions;
    private String currentWord;
    private Application application;

    public FilmListViewModel(Application application) {
        super(application);
        filmLiveData = new MutableLiveData<>();
        filmLiveDataExceptions = new MutableLiveData<>();
        this.application = application;
        currentWord = " ";
    }

    @SuppressLint("CheckResult")
    public void loadFilms(String keyWord) {
        Log.d(TAG, "loadFilms: ");
        if(!currentWord.equalsIgnoreCase(keyWord)){
            currentWord = keyWord;
            FilmRepository
                    .getInstance(application)
                    .getFilmInformationHolder(keyWord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<FilmInformationHolder>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<FilmInformationHolder> filmInformationHolders) {
                            filmLiveData.postValue(filmInformationHolders);
                        }

                        @Override
                        public void onError(Throwable e) {
                            filmLiveDataExceptions.postValue("Error while loading data: " + e.toString());
                        }
                    });
            //TODO check disposable
/*
            FilmRepository
                    .getInstance(application)
                    .getDatabase()
                    .filmsDAO()
                    .getAllFilms()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Film>>() {
                                   @Override
                                   public void accept(List<Film> films) throws Exception {
                                       filmLiveData.postValue(filmInformationHolders);
                                       Log.d("=+=+", "onNext: "+ films.toString());
                                   }
                               });
 */

        }
    }

    public MutableLiveData<List<FilmInformationHolder>> getFilmLiveData() {
        return filmLiveData;
    }

    public MutableLiveData<String> getFilmLiveDataExceptions() {
        return filmLiveDataExceptions;
    }

}