package com.example.internshipproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.internshipproject.R;
import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentFilmInformationBinding;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.viewmodels.FilmDetailsViewModel;



public class FilmInformationFragment extends Fragment implements View.OnClickListener {
    private FragmentFilmInformationBinding binding;
    private FilmDetails filmDetails;
    FilmDetailsViewModel viewModel;
    public static String DESCRIPTION = "description";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilmDetailsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilmInformationBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel
                    .getFilmDetailsById((String) bundle.getSerializable(VideoListAdapter.FILM_ID))
                    .observe(getViewLifecycleOwner(), new Observer<FilmDetails>() {
                                @Override
                                public void onChanged(FilmDetails filmDetails) {
                                    setFilmDetails(filmDetails);
                                    insertDataOnScreen();
                                }
                            }
                    );
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.descriptionDetails.setOnClickListener(this);
        binding.backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void insertDataOnScreen() {
        binding.yearText.setText(filmDetails.getYear());
        binding.releasedText.setText(filmDetails.getReleased());
        binding.genreText.setText(filmDetails.getGenre());
        binding.directorText.setText(filmDetails.getDirector());
        binding.writerText.setText(filmDetails.getWriter());
        binding.actorsText.setText(filmDetails.getActors());
        binding.titleText.setText(filmDetails.getTitle());
        Glide
                .with(getContext())
                .load(filmDetails.getPosterUrl())
                .into(binding.posterPhoto);
    }


    public void setFilmDetails(FilmDetails filmDetails) {
        this.filmDetails = filmDetails;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIPTION, filmDetails.getPlot());
        Navigation.findNavController(v).navigate(R.id.action_filmInformationFragment_to_filmDetailInformationFragment, bundle);
    }
}