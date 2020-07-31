package com.example.internshipproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.entities.FilmInformationHolder;
import com.example.internshipproject.repository.FilmRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FilmListViewModel extends ViewModel {
    //private MutableLiveData<List<Film>> filmLiveData;
    private MutableLiveData<List<FilmInformationHolder>> filmLiveData;
    private MutableLiveData<String> filmLiveDataExceptions;
    private String currentWord;

    public FilmListViewModel() {
        filmLiveData = new MutableLiveData<>();
        filmLiveDataExceptions = new MutableLiveData<>();
        currentWord = " ";
    }

    public void loadFilms(String keyWord) {
        if(!currentWord.equalsIgnoreCase(keyWord)){
            currentWord = keyWord;
            FilmRepository
                    .getInstance()
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
        }
    }

    public MutableLiveData<List<FilmInformationHolder>> getFilmLiveData() {
        return filmLiveData;
    }

    public MutableLiveData<String> getFilmLiveDataExceptions() {
        return filmLiveDataExceptions;
    }
}