package com.example.internshipproject.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Film.class, FilmDetails.class}, version = 2, exportSchema = false)
public abstract class FilmRoomDatabase extends RoomDatabase {
    public abstract FilmsDAO filmsDAO();
    private static FilmRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FilmRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (FilmRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FilmRoomDatabase.class, "film_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void insert(Film film){
        INSTANCE.filmsDAO().insertFilm(film);
    }

    public static void deleteAll(){
        INSTANCE.filmsDAO().deleteAllFromFilms();
    }

}
