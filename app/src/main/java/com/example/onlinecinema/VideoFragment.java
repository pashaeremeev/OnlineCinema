package com.example.onlinecinema;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.LoadControl;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlinecinema.entities.Movie;
import com.example.onlinecinema.entities.Quality;
import com.example.onlinecinema.repos.MovieRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

@UnstableApi public class VideoFragment extends Fragment {

    public static final String ID_KEY = "ID_KEY";

    private PlayerView playerView;
    private ProgressBar progressBar;
    private ExoPlayer player;
    private boolean isFullScreen = false;
    private Runnable runnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String tagFragment;

    public static VideoFragment getInstance(Integer movieId, String tagFragment) {
        VideoFragment videoFragment = new VideoFragment(tagFragment);
        Bundle bundle = new Bundle();
        bundle.putInt(ID_KEY, movieId);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    public VideoFragment(String tagFragment) {
        this.tagFragment = tagFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_video, container, false);

        container.setVisibility(View.VISIBLE);
        MovieRepo movieRepo = new MovieRepo();

        Integer movieId = getArguments().getInt(ID_KEY);
        Movie movie = movieRepo.getById(movieId);
        Uri movieUrl = Uri.parse(movie.getStream());

        playerView = fragment.findViewById(R.id.exoplayerView);
        progressBar = fragment.findViewById(R.id.progressBar);
        ImageView settingsBtn = playerView.findViewById(R.id.settingsBtn);
        ImageView fullScreenBtn = playerView.findViewById(R.id.fullscreenBtn);
        ImageView backBtn = playerView.findViewById(R.id.backBtn);
        TextView movieName = playerView.findViewById(R.id.name);
        TextView origName = playerView.findViewById(R.id.origName);
        movieName.setText(movie.getNameRu());
        if (movie.getOrigName() != null) {
            origName.setText(movie.getOrigName());
        } else {
            origName.setText("");
        }

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(getContext());
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());

        LoadControl loadControl = new DefaultLoadControl();

        player = new ExoPlayer.Builder(getContext())
                .setTrackSelector(trackSelector)
                .setLoadControl(loadControl)
                .build();
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        MediaItem mediaItem = MediaItem.fromUri(movieUrl);
        DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
                .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
                .setAllowCrossProtocolRedirects(true);
        MediaSource mediaSource = new HlsMediaSource.Factory(httpDataSourceFactory)
                .createMediaSource(mediaItem);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);

        fullScreenBtn.setOnClickListener(view -> {
            if (isFullScreen) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isFullScreen = false;
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isFullScreen = true;
            }
        });

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                removeFragment();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
                onDestroy();
                getActivity().onBackPressed();
            }
        });

        player.addListener(new Player.Listener() {

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(runnable, 1000);
                        }
                    };
                    handler.postDelayed(runnable, 0);
                    progressBar.setVisibility(View.GONE);
                } else if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        ImageView ppBtn = playerView.findViewById(R.id.exo_play);
        ppBtn.setOnClickListener(view -> {
            if (player.isPlaying()) {
                player.pause();
                player.setPlayWhenReady(false);
                ppBtn.setImageResource(R.drawable.ic_arrow_play);
            } else {
                player.play();
                player.setPlayWhenReady(true);
                ppBtn.setImageResource(R.drawable.ic_pause);
            }
        });

        settingsBtn.setOnClickListener(view -> {
            if (player.isPlaying()) {
                ArrayList<Quality> qualities = new ArrayList<>();
                for (int i = 0; i < player.getCurrentTracks().getGroups().get(0).length; i++) {
                    int height = player.getCurrentTracks().getGroups().get(0).getMediaTrackGroup().getFormat(i).height;
                    int width = player.getCurrentTracks().getGroups().get(0).getMediaTrackGroup().getFormat(i).width;
                    qualities.add(new Quality(width, height, i));
                }
                int currentIndex = qualities.size();
                qualities.add(new Quality(-1, -1, qualities.size()));
                for (int i = 0; i < player.getCurrentTracks().getGroups().get(0).length; i++) {
                    if (player.getCurrentTracks().getGroups().get(0).isTrackSelected(i)
                            && !(player.getTrackSelector().getParameters().overrides.isEmpty())) {
                        currentIndex = i;
                    }
                }
                if (!ItemQualityFragment.isExist) {
                    ItemQualityFragment itemQualityFragment = ItemQualityFragment.getInstance(qualities, currentIndex);
                    itemQualityFragment.show(getActivity().getSupportFragmentManager(), null);
                }
                getActivity().getSupportFragmentManager().setFragmentResultListener(ItemQualityFragment.REQUEST_KEY,
                        this, (requestKey, result) -> {
                            int index = result.getInt(ItemQualityFragment.BUNDLE_KEY);
                            if (index + 1 == qualities.size()) {
                                TrackSelectionParameters parameters = player.getTrackSelector().getParameters().buildUpon().clearOverrides().build();
                                player.getTrackSelector().setParameters(parameters);
                            } else {
                                TrackSelectionParameters parameters = player.getTrackSelector().getParameters().buildUpon().addOverride(new TrackSelectionOverride(
                                        player.getCurrentTracks().getGroups().get(0).getMediaTrackGroup(), index)).build();
                                player.getTrackSelector().setParameters(parameters);
                            }
                            getActivity().getSupportFragmentManager().clearFragmentResultListener(ItemQualityFragment.REQUEST_KEY);
                        });
            }
        });

        return fragment;
    }

    private void removeFragment() {
        Fragment fragment = getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(tagFragment);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        player.seekToDefaultPosition();
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        player.setPlayWhenReady(false);
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}