package com.example.internshipproject.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.internshipproject.entities.FilmPlayer;
import com.example.internshipproject.R;
import com.example.internshipproject.adapters.VideoListAdapter;
import com.example.internshipproject.databinding.FragmentFilmInformationBinding;
import com.example.internshipproject.entities.FilmDetails;
import com.example.internshipproject.viewmodels.FilmDetailsViewModel;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;


public class FilmInformationFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = FilmInformationFragment.class.getSimpleName();

    public static final String DESCRIPTION = "description";
    public static final String FILM_NAME = "film name";

    private FragmentFilmInformationBinding binding;
    private FilmDetails filmDetails;
    private FilmDetailsViewModel viewModel;
    public static boolean VIDEO_PLAYER_IS_SHOWED;

    private PlayerView playerView;
    private  FilmPlayer filmPlayer;

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
            viewModel.loadFilmDetailsById(bundle.getString(VideoListAdapter.FILM_ID));
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            int height = size.y;
            ViewGroup.LayoutParams layoutParams = binding.playerView.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            binding.playerView.setLayoutParams(layoutParams);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPlayerView();
        binding.descriptionDetails.setOnClickListener(this);
        binding.btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VIDEO_PLAYER_IS_SHOWED){
                    prepareScreenForFilm();
                } else {
                    prepareScreenForImage();
                }
                VIDEO_PLAYER_IS_SHOWED = !VIDEO_PLAYER_IS_SHOWED;
            }
        });
        if (filmPlayer.getPlayer() == null) {
            filmPlayer.initializePlayer();
        }
        filmPlayer.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    prepareScreenForImage();
                }
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            View decorView = getActivity().getWindow().getDecorView();
                            int visibility = View.VISIBLE;
                            decorView.setSystemUiVisibility(visibility);
                            Navigation.findNavController(binding.getRoot()).popBackStack();
                        }
                    }
                });
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
                                Toast.LENGTH_LONG ).show();
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
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.getRoot().setBackgroundColor(Color.BLACK);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

    /**
     * Initialize player view.
     */
    private void initPlayerView(){
        playerView = binding.playerView;
        playerView.setControllerAutoShow(false);
        playerView.setUseController(false);
        filmPlayer = FilmPlayer.getInstance(getContext());
        playerView.setPlayer(filmPlayer.getPlayer());
        playerView.setVisibility(View.INVISIBLE);
        if (VIDEO_PLAYER_IS_SHOWED){
            prepareScreenForFilm();
            filmPlayer.startPlayer();
        }
    }

}