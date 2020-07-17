package com.example.internshipproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.entities.Film;

import java.util.List;

public class FilmViewModel extends AndroidViewModel {
    private FilmRepository repository;
    private LiveData<List<Film>> films;

    public FilmViewModel(@NonNull Application application) {
        super(application);
        repository = new FilmRepository();
        films = repository.getFilmList();
    }

    public LiveData<List<Film>> getFilms() {
        return films;
    }

    public void setFilms(LiveData<List<Film>> films) {
        this.films = films;
    }
}
