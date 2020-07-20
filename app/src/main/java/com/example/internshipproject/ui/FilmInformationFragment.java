package com.example.internshipproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.internshipproject.databinding.FragmentFilmInformationBinding;
import com.example.internshipproject.entities.Film;
import com.example.internshipproject.entities.FilmDetails;


public class FilmInformationFragment extends Fragment {
    private FragmentFilmInformationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilmInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void insertDataOnScreen(FilmDetails filmDetails){
        binding.yearText.    setText(filmDetails.getYear());
        binding.releasedText.setText(filmDetails.getReleased());
        binding.genreText.   setText(filmDetails.getGenre());
        binding.directorText.setText(filmDetails.getDirector());
        binding.writerText.  setText(filmDetails.getWriter());
        binding.actorsText.  setText(filmDetails.getActors());
    }
}