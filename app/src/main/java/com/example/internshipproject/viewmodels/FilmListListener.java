package com.example.internshipproject.viewmodels;

import com.example.internshipproject.entities.Film;

import java.util.List;

/**
 * Class wrapper for waiting getting data from asynchronous methods.
 */
public interface FilmListListener {
    /**
     * Waits for loading detail data about list of films from server and puts it into
     * {@param filmList}.
     *
     * @param filmList is a List of Film objects which data from asynchronous methods puts in.
     */
    void loadFilmList(List<Film> filmList);

    /**
     * Notifies about getting from server response that doesn't contain a list of films.
     *
     * @param response is a message that user will see.
     */
    void notifyAboutNotSuccessfulResponse(String response);
}
