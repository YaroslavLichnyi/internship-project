package com.example.internshipproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.entities.Search;
import com.example.internshipproject.repository.FilmRepository;
import com.example.internshipproject.entities.Film;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FilmListViewModel extends ViewModel {
    private MutableLiveData<List<Film>> filmLiveData;
    private MutableLiveData<String> filmLiveDataExceptions;
    private String currentWord;

    public FilmListViewModel() {
        filmLiveData = new MutableLiveData<>();
        filmLiveDataExceptions = new MutableLiveData<>();
    }

    public LiveData<List<Film>> loadFilms(String keyWord) {
        currentWord = keyWord;
        if(currentWord.equalsIgnoreCase(keyWord)){
            FilmRepository.getInstance().getFilmLiveData(keyWord).subscribe(new Observer<Search>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Search search) {
                    filmLiveData.postValue(search.getSearch());
                }

                @Override
                public void onError(Throwable e) {
                    filmLiveDataExceptions.postValue("Error while loading data: " + e.toString());
                }

                @Override
                public void onComplete() {

                }
            });
        }
        return filmLiveData;
    }

    public MutableLiveData<List<Film>> getFilmLiveData() {
        return filmLiveData;
    }

    public MutableLiveData<String> getFilmLiveDataExceptions() {
        return filmLiveDataExceptions;
    }
}