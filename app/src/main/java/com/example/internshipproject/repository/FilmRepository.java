package com.example.internshipproject.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.entities.FilmInformationHolder;
import com.example.internshipproject.entities.Search;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


public class FilmRepository {
    private static final String TAG = FilmRepository.class.getSimpleName();

    /**
     * Contains a base url that Retrofit uses for connecting to server
     */
    public final String BASE_URL = "https://www.omdbapi.com/";
    private static FilmRepository instance;
    private MutableLiveData<List<Film>> filmLiveData;
    private List<Film> films;
    private FilmDetails filmDetails;
    private Disposable subscription;

    private FilmRepository() {
        filmLiveData = new MutableLiveData<>();
    }

    /**
     * This method is using for implementing Observer pattern.
     *
     * @return an instance of {@link FilmRepository}. It is only one object of {@link FilmRepository}
     * class.
     */
    public static FilmRepository getInstance(){
        if (instance == null){
            instance = new FilmRepository();
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
     * @return a list of {@link FilmInformationHolder} every item of which contains information
     * about film and detail information about it.
     */
    public Single<List<FilmInformationHolder>> getFilmInformationHolder(String keyWord){
        return FilmRepository
                .getInstance()
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
                        return FilmRepository.getInstance().getFilmDetailsById(film.getImdbID()).map(new Function<FilmDetails, FilmInformationHolder>() {
                            @Override
                            public FilmInformationHolder apply(FilmDetails filmDetails) throws Exception {
                                return new FilmInformationHolder(film, filmDetails);
                            }
                        });
                    }
                })
                .toList();
    }
}