package com.example.internshipproject.entities;

import androidx.room.Entity;


public class FilmInformationHolder {
    private Film film;
    private FilmDetails filmDetails;

    public FilmInformationHolder(Film film, FilmDetails filmDetails) {
        this.film = film;
        this.filmDetails = filmDetails;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public FilmDetails getFilmDetails() {
        return filmDetails;
    }

    public void setFilmDetails(FilmDetails filmDetails) {
        this.filmDetails = filmDetails;
    }
}