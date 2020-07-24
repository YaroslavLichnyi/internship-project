package com.example.internshipproject.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentRecyclerViewBinding;
import com.example.internshipproject.entities.Film;
import com.example.internshipproject.viewmodels.FilmListViewModel;

import java.util.List;


public class RecyclerViewFragment extends Fragment {
    private FragmentRecyclerViewBinding recyclerViewBinding;
    private FilmListViewModel viewModel;
    private List<Film> filmList;
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilmListViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeOnLiveData();
        subscribeOnLiveDataExceptions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerViewBinding = FragmentRecyclerViewBinding.inflate(inflater, container, false);
        initRecyclerView();
        if(filmList == null){
            viewModel.loadFilms("war");
        }
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
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    /**
     * Binds a variable recyclerView with recyclerView view and set an Adapter into it.
     */
    private void initRecyclerView(){
        adapter = new VideoListAdapter();
        recyclerView = recyclerViewBinding.recyclerView;
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    /**
     *  Inserts data about films into recyclerView.
     * @param films contains data that inserts into recyclerView.
     */
    private void insertFilmsIntoRecyclerView(List<Film> films){
        adapter.setFilmList(films);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Subscribes on LiveData updates. When LiveData updates then new data that LiveData contains
     * displays on the screen.
     */
    private void subscribeOnLiveData(){
        viewModel
                .getFilmLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<Film>>() {
                    @Override
                    public void onChanged(List<Film> films) {
                        setFilmList(films);
                        insertFilmsIntoRecyclerView(films);
                    }
                });
    }

    /**
     * Subscribes on LiveData updates about Exceptions and Errors that are happen while LiveData
     * tries to get data from server.
     */
    private void subscribeOnLiveDataExceptions(){
        viewModel
                .getFilmLiveDataExceptions()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(
                                getContext(),
                                "Couldn't update the film list:" + s,
                                Toast.LENGTH_LONG );
                    }
                });
    }
}