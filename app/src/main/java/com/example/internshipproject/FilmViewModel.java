package com.example.internshipproject;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.internshipproject.entities.Film;

import java.util.List;

public class FilmViewModel extends AndroidViewModel {
    private FilmRepository repository;

    public FilmViewModel(@NonNull Application application) {
        super(application);
        repository = new FilmRepository();
    }

    public LiveData<List<Film>> getFilms() {
        return repository.getFilmLiveData();
    }
}
