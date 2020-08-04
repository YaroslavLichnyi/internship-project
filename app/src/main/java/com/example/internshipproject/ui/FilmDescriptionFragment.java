package com.example.internshipproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.internshipproject.databinding.FragmentFilmDescriptionBinding;

public class FilmDescriptionFragment extends Fragment implements View.OnClickListener {
    private FragmentFilmDescriptionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilmDescriptionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        binding.descriptionText.setText(bundle.getString(FilmInformationFragment.DESCRIPTION));
        binding.filmName.setText(bundle.getString(FilmInformationFragment.FILM_NAME));
        binding.backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Navigation.findNavController(v).popBackStack();
    }
}