package com.example.internshipproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.internshipproject.databinding.FragmentFilmDetailInformationBinding;

public class FilmDetailInformationFragment extends Fragment {
    private FragmentFilmDetailInformationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilmDetailInformationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        binding.textView2.setText((String)bundle.getSerializable(FilmInformationFragment.DESCRIPTION));
    }
}