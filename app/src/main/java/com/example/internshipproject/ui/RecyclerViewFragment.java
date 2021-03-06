package com.example.internshipproject.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

import com.example.internshipproject.adapters.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentRecyclerViewBinding;
import com.example.internshipproject.entities.FilmInformationHolder;
import com.example.internshipproject.viewmodels.FilmListViewModel;

import java.util.List;


public class RecyclerViewFragment extends Fragment {
    public static int lastPosition = 0;
    private FragmentRecyclerViewBinding recyclerViewBinding;
    private FilmListViewModel viewModel;
    private List<FilmInformationHolder> filmList;
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;
    private SearchView searchView;
    private boolean searchViewIsWide;

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
            viewModel.loadFilms("Avengers");
        }
        return recyclerViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = recyclerViewBinding.searchView;
        recyclerViewBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.loadFilms(query);
                recyclerView.scrollToPosition(0);
                shortSearchView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!searchViewIsWide){
                    wideSearchView();
                }
                return false;
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });
        searchView.setOnTouchListener((v, event) -> {
            wideSearchView();
            return true;
        });
        recyclerViewBinding.buttonFind.setOnClickListener(v -> {
            shortSearchView();
            viewModel.loadFilms(String.valueOf(recyclerViewBinding.searchView.getQuery()));
            recyclerView.scrollToPosition(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.sSelectedHolderPosition > 0
                && adapter.getItemCount() > adapter.sSelectedHolderPosition){
            recyclerView.scrollToPosition(adapter.sSelectedHolderPosition);
        }
    }

    public void setFilmList(List<FilmInformationHolder> filmList) {
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
    private void insertFilmsIntoRecyclerView(List<FilmInformationHolder> films){
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
                .observe(getViewLifecycleOwner(), new Observer<List<FilmInformationHolder>>() {
                    @Override
                    public void onChanged(List<FilmInformationHolder> filmInformationHolders) {
                        setFilmList(filmInformationHolders);
                        insertFilmsIntoRecyclerView(filmInformationHolders);
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
                                "Couldn't update the film list:" + s.toString(),
                                Toast.LENGTH_LONG ).show();
                        //TODO not to show the mistake
                    }
                });
    }

    /**
     * Makes App name not displaying what gives an opportunity for searchView to take up the
     * whole width of the screen.
     */
    private void wideSearchView(){
        ViewGroup.LayoutParams layoutParams = recyclerViewBinding.appName.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.width = 0;
        recyclerViewBinding.appName.setLayoutParams(layoutParams);
        searchViewIsWide = true;
    }

    /**
     * Makes App name's width enough for displaying it what allows searchView to take up only the
     * rest of the screen's width.
     */
    private void shortSearchView(){
        ViewGroup.LayoutParams layoutParams = recyclerViewBinding.appName.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        recyclerViewBinding.appName.setLayoutParams(layoutParams);
        searchViewIsWide = false;
    }
}