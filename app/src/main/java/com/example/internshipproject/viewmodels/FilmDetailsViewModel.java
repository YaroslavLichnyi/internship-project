package com.example.internshipproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.FilmRepository;
import com.example.internshipproject.entities.FilmDetails;

import java.util.List;

public class FilmDetailsViewModel extends ViewModel {
    private MutableLiveData<FilmDetails> filmDetailsLiveData;

    public FilmDetailsViewModel() {
        filmDetailsLiveData = new MutableLiveData<>();
    }

    public LiveData<FilmDetails> getFilmDetailsById(String id) {
        if(filmDetailsLiveData.getValue() == null){
            FilmRepository.getInstance().getFilmDetailsById(id, new FilmDetailsListener() {
                @Override
                public void loadFilmDetails(FilmDetails filmDetails) {
                    filmDetailsLiveData.postValue(filmDetails);
                }
            });
        }
        return filmDetailsLiveData;
    }

}
