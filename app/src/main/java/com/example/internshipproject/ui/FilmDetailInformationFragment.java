package com.example.internshipproject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.internshipproject.R;
import com.example.internshipproject.databinding.FragmentFilmDetailInformationBinding;

public class FilmDetailInformationFragment extends Fragment {
    private FragmentFilmDetailInformationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilmDetailInformationBinding.inflate(inflater, container, true);
        View view = binding.getRoot();
        return view;
    }
}