package com.example.internshipproject.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.db.FilmRoomDatabase;
import com.example.internshipproject.db.FilmsDAO;
import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.FilmInformationHolder;
import com.example.internshipproject.entities.Search;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;


public class FilmRepository {
    private static final String TAG = FilmRepository.class.getSimpleName();


    /**
     * Contains a base url that Retrofit uses for connecting to server
     */
    public final String BASE_URL = "https://www.omdbapi.com/";
    private static FilmRepository instance;
    private MutableLiveData<List<Film>> filmLiveData;
    private FilmsDAO filmsDAO;
    private Context context;
    private FilmRoomDatabase database;

    private FilmRepository() {
    }

    /**
     * This method is using for implementing Observer pattern.
     *
     * @return an instance of {@link FilmRepository}. It is only one object of {@link FilmRepository}
     * class.
     */
    public static FilmRepository getInstance(Context context){
        if (instance == null){
            instance = new FilmRepository();
            instance.context = context;
            instance.filmLiveData = new MutableLiveData<>();
            instance.database = FilmRoomDatabase.getDatabase(context);
            instance.filmsDAO = instance.database.filmsDAO();

            //instance.filmLiveData = instance.filmsDAO.
        }
        if (instance.context == null){
            instance.context = context;
        }
        return instance;
    }

    /**
     * Gets {@link Observable} that contains data about films by keyword.
     *
     * @param keyWord is a word or a set of words which are used for finding films.
     * @return an observable of films that fits keyWord
     */
    public Observable<Search> getSearch(String keyWord) {
        return RetrofitService
                .getInstance()
                .getFilmsInfoDownloader(keyWord);
    }

    /**
     * Gets {@link Observable} that contains data about film details by film's id.
     *
     * @param id is a unique id of film. Film is searched by it.
     * @return
     */
    public Observable<FilmDetails> getFilmDetailsById(String id){
        return RetrofitService
                .getInstance()
                .getFilmDetailsInfoDownloader(id);
    }

    /**
     * Gets a {@link Single} of films and detail information about them by keyword.
     *
     * @param keyWord is a word or a set of words which are used for finding films.
     * @return a {@link Single} which contains list of {@link FilmInformationHolder} every item of which contains information
     * about film and detail information about it.
     */
    public static Single<List<FilmInformationHolder>> getFilmInformationHolder(String keyWord){
        instance.deleteAllFilms();
        instance.deleteAllFilmDetails();
        return instance
                .getSearch(keyWord)
                .map(new Function<Search, List<Film>>() {
                    @Override
                    public List<Film> apply(Search search) throws Exception {
                        return search.getFilmList();
                    }
                })
                .flatMap(Observable::fromIterable)
                .flatMap(new Function<Film, ObservableSource<FilmInformationHolder>>() {
                    @Override
                    public ObservableSource<FilmInformationHolder> apply(Film film) throws Exception {
                        instance.insert(film);
                        return FilmRepository
                                .getInstance(instance.context)
                                .getFilmDetailsById(film.getImdbId())
                                .map(new Function<FilmDetails, FilmInformationHolder>() {
                            @Override
                            public FilmInformationHolder apply(FilmDetails filmDetails) throws Exception {
                                filmDetails.setImdbId(film.getImdbId());
                                instance.insert(filmDetails);
                                return new FilmInformationHolder(film, filmDetails);
                            }
                        });
                    }
                })
                .toList();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FilmRoomDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FilmRoomDatabase database) {
        this.database = database;
    }

    public void insert(Film film){
        FilmRoomDatabase.databaseWriteExecutor.execute(() -> instance.filmsDAO.insertFilm(film));
    }

    public void insert(FilmDetails filmDetails){
        FilmRoomDatabase.databaseWriteExecutor.execute(() -> instance.filmsDAO.insertFilmDetails(filmDetails));
    }

    public void deleteAllFilms(){
        FilmRoomDatabase.databaseWriteExecutor.execute(() -> instance.filmsDAO.deleteAllFromFilms());
    }

    public void deleteAllFilmDetails(){
        FilmRoomDatabase.databaseWriteExecutor.execute(() -> instance.filmsDAO.deleteAllFromFilmDetails());
    }
}