package com.example.internshipproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.internshipproject.repository.FilmRepository;
import com.example.internshipproject.entities.FilmDetails;

public class FilmDetailsViewModel extends ViewModel {
    private MutableLiveData<FilmDetails> filmDetailsLiveData;
    private MutableLiveData<String> filmDetailsLiveDataException;

    public FilmDetailsViewModel() {
        filmDetailsLiveData = new MutableLiveData<>();
        filmDetailsLiveDataException = new MutableLiveData<>();
    }

    public LiveData<FilmDetails> loadFilmDetailsById(String id) {
        FilmRepository.getInstance().getFilmDetailsById(id, new FilmDetailsListener() {
            @Override
            public void loadFilmDetails(FilmDetails filmDetails) {
                filmDetailsLiveData.postValue(filmDetails);
            }

            @Override
            public void notifyAboutNotSuccessfulResponse(String response) {
                filmDetailsLiveDataException.postValue(response);
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
