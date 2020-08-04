package com.example.internshipproject.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FilmsDAO {

    @Insert
    void insertFilm(Film film);

    @Insert
    void insertFilmDetails(FilmDetails filmDetails);

    @Delete
    void deleteFilm(Film film);

    @Delete
    void deleteFilmDetails(FilmDetails filmDetails);

    @Query("SELECT * FROM films")
    Flowable<List<Film>> getAllFilms();

    @Query("SELECT * FROM film_details")
    Flowable<List<FilmDetails>> getAllFilmDetails();

    @Query("SELECT * FROM film_details WHERE imdbId = :imdbId")
    Flowable<FilmDetails> findFilmDetailsById(String imdbId);

    @Query("SELECT * FROM films WHERE imdbId = :imdbId")
    Flowable<Film> findFilmById(String imdbId);

    @Query("DELETE FROM films")
    void deleteAllFromFilms();

    @Query("DELETE FROM film_details")
    void deleteAllFromFilmDetails();
}
