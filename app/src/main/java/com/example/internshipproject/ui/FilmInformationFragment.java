package com.example.internshipproject.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.internshipproject.entities.Film;
import com.example.internshipproject.viewmodels.FilmDetailsViewModel;
import com.example.internshipproject.viewmodels.FilmListViewModel;
import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentFilmInformationBinding;
import com.example.internshipproject.entities.FilmDetails;

import java.util.List;


public class FilmInformationFragment extends Fragment {
    private FragmentFilmInformationBinding binding;
    private FilmDetails filmDetails;
    FilmDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilmDetailsViewModel.class);

        /*viewModel.getFilmDetailsById((String) bundle.getSerializable(VideoListAdapter.FILM_ID)).observe(
                getViewLifecycleOwner(),
                new Observer<List<Film>>() {
                    @Override
                    public void onChanged(List<Film> films) {
                        initRecyclerView(films);
                    }
                });

         */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilmInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*viewModel = ViewModelProviders.of(this).get(FilmViewModel.class);
        filmDetails =  viewModel.getFilmDetailsById(
                savedInstanceState.getString(VideoListAdapter.FILM_ID));*/
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel
                    .getFilmDetailsById((String) bundle.getSerializable(VideoListAdapter.FILM_ID))
                    .observe(getViewLifecycleOwner(), new Observer<FilmDetails>() {
                                @Override
                                public void onChanged(FilmDetails filmDetails) {
                                    setFilmDetails(filmDetails);
                                }
                            }
                    );
        }
        Log.d("cccccccc", "onViewCreated: " + filmDetails.toString());
        //insertDataOnScreen();
    }

    private void insertDataOnScreen() {
        binding.yearText.setText(filmDetails.getYear());
        binding.releasedText.setText(filmDetails.getReleased());
        binding.genreText.setText(filmDetails.getGenre());
        binding.directorText.setText(filmDetails.getDirector());
        binding.writerText.setText(filmDetails.getWriter());
        binding.actorsText.setText(filmDetails.getActors());
    }

    public FilmDetails getFilmDetails() {
        return filmDetails;
    }

    public void setFilmDetails(FilmDetails filmDetails) {
        this.filmDetails = filmDetails;
    }
}