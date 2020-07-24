package com.example.internshipproject.viewmodels;

import com.example.internshipproject.entities.FilmDetails;

/**
 * Class wrapper for waiting getting data from asynchronous methods.
 */
public interface FilmDetailsListener {
    /**
     * Waits for loading detail data about film from server and puts it into {@param filmDetails}.
     *
     * @param filmDetails is a FilmDetails object which data from asynchronous methods puts in.
     */
    void loadFilmDetails(FilmDetails filmDetails);

    /**
     * Notifies about getting from server response that doesn't contain detail information about
     * films.
     *
     * @param response is a message that user will see.
     */
    void notifyAboutNotSuccessfulResponse(String response);
}
