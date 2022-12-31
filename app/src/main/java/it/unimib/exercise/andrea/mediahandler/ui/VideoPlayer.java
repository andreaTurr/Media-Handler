package it.unimib.exercise.andrea.mediahandler.ui;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.databinding.ActivityMainBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VideoPlayer extends Fragment{
    private YouTubePlayerView youTubePlayerView;
    private MaterialToolbar topAppbar;
    private BottomNavigationView bottomNav;
    private static final String TAG = VideoPlayer.class.getSimpleName();

    public VideoPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNav = getActivity().findViewById(R.id.bottom_navigation);
        topAppbar = getActivity().findViewById(R.id.top_appbar);
        setBarsBaseOnRotation();
        String playlistId = VideoPlayerArgs.fromBundle(getArguments()).getPlaylistId();
        String videoId = VideoPlayerArgs.fromBundle(getArguments()).getVideoId();

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                Snackbar.make(view, videoId, Snackbar.LENGTH_SHORT).show();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        super.onCreate(savedInstanceState);

    }

    private void setBarsBaseOnRotation() {
        Display display = ((WindowManager) getActivity().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        Log.d(TAG, "setBarsBaseOnRotation: " + orientation);
        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            hideSystemBars();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: ");
        int newOrientation = newConfig.orientation;
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemBars();
            youTubePlayerView.enterFullScreen();
        }else{
            showSystemBars();
            youTubePlayerView.exitFullScreen();
        }
    }

    private void hideSystemBars() {
        Log.d(TAG, "onConfigurationChanged: landscape");
        bottomNav.setVisibility(View.GONE);
        topAppbar.setVisibility(View.GONE);
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        requireActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void showSystemBars() {
        Log.d(TAG, "onConfigurationChanged: horizontal");
        bottomNav.setVisibility(View.VISIBLE);
        topAppbar.setVisibility(View.VISIBLE);
        View decorView = requireActivity().getWindow().getDecorView();
        //Log.d(TAG, "onConfigurationChanged: " + decorView.getSystemUiVisibility());
        int flags = View.SYSTEM_UI_FLAG_VISIBLE;
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public void onDestroyView() {
        showSystemBars();
        super.onDestroyView();
    }
}