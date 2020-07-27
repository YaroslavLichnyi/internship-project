package com.example.internshipproject.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.internshipproject.BuildConfig;
import com.example.internshipproject.FilmPlayer;
import com.example.internshipproject.R;
import com.example.internshipproject.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentFilmInformationBinding;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.viewmodels.FilmDetailsViewModel;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class FilmInformationFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = FilmInformationFragment.class.getSimpleName();

    public static String DESCRIPTION = "description";
    public static String FILM_NAME = "film name";

    private FragmentFilmInformationBinding binding;
    private FilmDetails filmDetails;
    private FilmDetailsViewModel viewModel;
    private static boolean videoPlayerIsShowed;

    private PlayerView playerView;
    private FilmPlayer filmPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilmDetailsViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeOnLiveData();
        subscribeOnLiveDataExceptions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFilmInformationBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel
                    .loadFilmDetailsById((String) bundle.getSerializable(VideoListAdapter.FILM_ID))
                    .observe(getViewLifecycleOwner(), new Observer<FilmDetails>() {
                                @Override
                                public void onChanged(FilmDetails filmDetails) {
                                    setFilmDetails(filmDetails);
                                    insertDataOnScreen();
                                }
                            }
                    );
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            binding.playerView.getLayoutParams().height = height;
            binding.playerView.getLayoutParams().width = width;
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerView = binding.playerView;
        filmPlayer = FilmPlayer.newInstance(getActivity().getApplicationContext());
        filmPlayer.initializePlayer();
        playerView.setPlayer(filmPlayer.getPlayer());

        binding.descriptionDetails.setOnClickListener(this);
        playerView.setVisibility(View.INVISIBLE);
        //binding.backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        if (videoPlayerIsShowed){
            prepareScreenForFilm();
            filmPlayer.startPlayer();

        }
        binding.btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoPlayerIsShowed){
                    prepareScreenForFilm();
                } else {
                    prepareScreenForImage();
                }
                videoPlayerIsShowed = !videoPlayerIsShowed;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //filmPlayer.initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || filmPlayer.getPlayer() == null)) {
            filmPlayer.initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            filmPlayer.releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            filmPlayer.releasePlayer();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(DESCRIPTION, filmDetails.getPlot());
        bundle.putString(FILM_NAME, filmDetails.getTitle());
        Navigation
                .findNavController(v)
                .navigate(
                        R.id.action_filmInformationFragment_to_filmDetailInformationFragment,
                        bundle);
    }

    public void setFilmDetails(FilmDetails filmDetails) {
        this.filmDetails = filmDetails;
    }

    /**
     * Put values from {@filmDetails} to fields that contains information about it.
     */
    private void insertDataOnScreen() {
        binding.yearText    .setText(filmDetails.getYear());
        binding.releasedText.setText(filmDetails.getReleased());
        binding.genreText   .setText(filmDetails.getGenre());
        binding.runtimeText .setText(filmDetails.getRuntime());
        binding.directorText.setText(filmDetails.getDirector());
        binding.writerText  .setText(filmDetails.getWriter());
        binding.actorsText  .setText(filmDetails.getActors());
        binding.titleText   .setText(filmDetails.getTitle());
        Glide
                .with(getContext())
                .load(filmDetails.getPosterUrl())
                .into(binding.posterPhoto);
    }

    /**
     * Subscribes on LiveData updates. When LiveData updates then new data that LiveData contains
     * displays on the screen.
     */
    private void subscribeOnLiveData(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel
                    .loadFilmDetailsById((String) bundle.getSerializable(VideoListAdapter.FILM_ID))
                    .observe(getViewLifecycleOwner(), new Observer<FilmDetails>() {
                                @Override
                                public void onChanged(FilmDetails filmDetails) {
                                    setFilmDetails(filmDetails);
                                    insertDataOnScreen();
                                }
                            }
                    );
        }
    }

    /**
     * Subscribes on LiveData updates about Exceptions and Errors that are happen while LiveData
     * tries to get data from server.
     */
    private void subscribeOnLiveDataExceptions(){
        viewModel
                .getFilmDetailsLiveDataException()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(
                                getContext(),
                                "Couldn't update detail information about film:" + s,
                                Toast.LENGTH_LONG );
                    }
                });
    }

    /**
     * Prepares UI for displaying video content. Background becomes black to male watching video
     * content more comfort. Also,while watching video in landscape orientation status bar hides.
     */
    private void prepareScreenForFilm(){
        filmPlayer.startPlayer();
        playerView.setVisibility(View.VISIBLE);
        binding.posterPhoto.setVisibility(View.INVISIBLE);
        playerView.hideController();
        binding.btPlay.setText("Show poster");
        binding.btPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_image_24, 0, 0, 0);
        binding.getRoot().setBackgroundColor(Color.BLACK);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View decorView = getActivity().getWindow().getDecorView();
            int visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(visibility);
        }
    }

    /**
     * Prepares UI for displaying poster image. If status bar was hide while watching video in
     * landscape orientation it becomes not hidden.
     */
    private void prepareScreenForImage(){
        filmPlayer.pausePlayer();
        playerView.setVisibility(View.INVISIBLE);
        binding.posterPhoto.setVisibility(View.VISIBLE);
        binding.btPlay.setText("Play film");
        binding.btPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_arrow_24, 0, 0, 0);
        binding.getRoot().setBackgroundResource(R.color.colorDarkGreyBackground);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View decorView = getActivity().getWindow().getDecorView();
            int visibility = View.VISIBLE;
            decorView.setSystemUiVisibility(visibility);
        }
    }
}