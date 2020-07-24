package com.example.internshipproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.repository.FilmRepository;
import com.example.internshipproject.entities.Film;

import java.util.List;

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
            FilmRepository.getInstance().getFilmLiveData(keyWord, new FilmListListener() {
                @Override
                public void loadFilmList(List<Film> filmList) {
                    filmLiveData.postValue(filmList);
                }

                @Override
                public void notifyAboutNotSuccessfulResponse(String response) {
                    filmLiveDataExceptions.postValue(response);
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