package com.example.internshipproject.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.internshipproject.BuildConfig;
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

    private FragmentFilmInformationBinding binding;
    private FilmDetails filmDetails;
    private FilmDetailsViewModel viewModel;
    private boolean videoPlayerIsShowed;

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerView = binding.playerView;
        binding.descriptionDetails.setOnClickListener(this);
        //binding.backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoPlayerIsShowed){
                    startPlayer();
                    playerView.setVisibility(View.VISIBLE);
                    binding.posterPhoto.setVisibility(View.INVISIBLE);
                    binding.btPlay.setText("Close player");
                } else {
                    pausePlayer();
                    playerView.setVisibility(View.INVISIBLE);
                    binding.posterPhoto.setVisibility(View.VISIBLE);
                    binding.btPlay.setText("Play");
                }
                videoPlayerIsShowed = !videoPlayerIsShowed;
            }
        });
        playerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIPTION, filmDetails.getPlot());
        Navigation.findNavController(v).navigate(R.id.action_filmInformationFragment_to_filmDetailInformationFragment, bundle);
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


    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        Uri uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4");
        MediaSource mediaSource = buildMediaSource(uri);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    /**
     * Builds media source
     * @param uri
     * @return
     */
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(), "yourApplicationName"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * Sets player on pause.
     */
    private void pausePlayer(){
        if (BuildConfig.DEBUG) Log.d(TAG, "pausePlayer: player sets on pause");
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    /**
     * Starts player playing.
     */
    private void startPlayer(){
        if (BuildConfig.DEBUG) Log.d(TAG, "startPlayer: player is starting");
        player.setPlayWhenReady(true);
        player.getPlaybackState();
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
}