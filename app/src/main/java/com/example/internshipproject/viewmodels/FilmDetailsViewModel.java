package com.example.internshipproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.repository.FilmRepository;
import com.example.internshipproject.entities.FilmDetails;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FilmDetailsViewModel extends ViewModel {
    private MutableLiveData<FilmDetails> filmDetailsLiveData;
    private MutableLiveData<String> filmDetailsLiveDataException;

    public FilmDetailsViewModel() {
        filmDetailsLiveData = new MutableLiveData<>();
        filmDetailsLiveDataException = new MutableLiveData<>();
    }

    public LiveData<FilmDetails> loadFilmDetailsById(String id) {
        Log.d("TAG", "loadFilmDetailsById: ");
        FilmRepository
                .getInstance()
                .getFilmDetailsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilmDetails>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(FilmDetails filmDetails) {
                filmDetailsLiveData.postValue(filmDetails);
            }

            @Override
            public void onError(Throwable e) {
                filmDetailsLiveDataException.postValue("Error while loading data: " + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
        return filmDetailsLiveData;
    }

    public MutableLiveData<FilmDetails> getFilmDetailsLiveData() {
        return filmDetailsLiveData;
    }

    public MutableLiveData<String> getFilmDetailsLiveDataException() {
        return filmDetailsLiveDataException;
    }


}
