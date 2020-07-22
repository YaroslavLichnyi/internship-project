package com.example.internshipproject.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internshipproject.viewmodels.FilmListViewModel;
import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentRecyclerViewBinding;
import com.example.internshipproject.entities.Film;

import java.util.List;


public class RecyclerViewFragment extends Fragment {
    private FragmentRecyclerViewBinding recyclerViewBinding;
    private FilmListViewModel viewModel;
    private List<Film> filmList;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerViewBinding = FragmentRecyclerViewBinding.inflate(inflater, container, false);
        viewModel = ViewModelProviders.of(this).get(FilmListViewModel.class);
        viewModel
                .getFilms("war")
                .observe(
                getViewLifecycleOwner(), new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
                initRecyclerView();
                insertFilmsIntoRecyclerView(films);
            }
        });
        return recyclerViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView = recyclerViewBinding.searchView;
                viewModel.getFilms((String) searchView.getQuery()).observe(
                        getViewLifecycleOwner(),
                        new Observer<List<Film>>() {
                            @Override
                            public void onChanged(List<Film> films) {
                                initRecyclerView(films);
                            }
                        });
            }
        });
         */
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });
    }

    private void initRecyclerView(){
        adapter = new VideoListAdapter();
        recyclerView = recyclerViewBinding.recyclerView;
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void insertFilmsIntoRecyclerView(List<Film> films){
        adapter.setFilmList(films);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        adapter.notifyDataSetChanged();
    }
}