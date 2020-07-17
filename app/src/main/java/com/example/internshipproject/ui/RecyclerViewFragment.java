package com.example.internshipproject.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentRecyclerViewBinding;


public class RecyclerViewFragment extends Fragment {
    private FragmentRecyclerViewBinding recyclerViewBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerViewBinding = FragmentRecyclerViewBinding.inflate(inflater, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = recyclerViewBinding.recyclerView;
        VideoListAdapter adapter = new VideoListAdapter(((MainActivity)getActivity()).getViewModel().getFilms());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter.setFilms(((MainActivity)getActivity()).getViewModel().getFilms());
        adapter.notifyDataSetChanged();
    }
}