package com.example.internshipproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.FilmRepository;
import com.example.internshipproject.entities.Film;

import java.util.List;

public class FilmListViewModel extends ViewModel {
    private MutableLiveData<List<Film>> filmLiveData;

    public FilmListViewModel() {
        filmLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Film>> getFilms(String keyWord) {
        if(filmLiveData.getValue() == null){
            FilmRepository.getInstance().getFilmLiveData(keyWord, new FilmListListener() {
                @Override
                public void loadFilmList(List<Film> filmList) {
                    filmLiveData.postValue(filmList);
                }
            });
        }
        return filmLiveData;
    }

}