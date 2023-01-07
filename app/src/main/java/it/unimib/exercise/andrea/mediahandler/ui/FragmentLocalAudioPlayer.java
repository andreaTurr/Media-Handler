package it.unimib.exercise.andrea.mediahandler.ui;

import static android.content.Context.WINDOW_SERVICE;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.repository.IMediaRepository;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLocalAudioPlayer extends Fragment {
    private StyledPlayerView playerView;
    private LocalAudio localAudio;
    private ExoPlayer player;
    private ViewModelMedia viewModelMedia;
    private MaterialToolbar topAppbar;
    private BottomNavigationView bottomNav;
    private static final String TAG = FragmentLocalAudioPlayer.class.getSimpleName();

    public FragmentLocalAudioPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IMediaRepository mediaRepository =
                ServiceLocator.getInstance().getMediaRepository(
                        requireActivity().getApplication()
                );
        if (mediaRepository != null){
            viewModelMedia = new ViewModelProvider(
                    requireActivity(),
                    new ViewModelMediaFactory(mediaRepository)).get(ViewModelMedia.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNav = getActivity().findViewById(R.id.bottom_navigation);
        topAppbar = getActivity().findViewById(R.id.top_appbar);
        localAudio = FragmentLocalAudioPlayerArgs.fromBundle(getArguments()).getLocalAudio();
        playerView = view.findViewById(R.id.player_view);

        player = new ExoPlayer.Builder(getContext()).build();

        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(localAudio.getUri());
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();


        playerView.setPlayer(player);

        // Start the playback.
        Log.d(TAG, "onViewCreated: pre getLocalAudioUri=" + localAudio.getUri());
        viewModelMedia.getLocalAudio(localAudio).observe(getViewLifecycleOwner(), resultLocalAudios -> {
            if(resultLocalAudios.isSuccess()){
                Log.d(TAG, "onViewCreated: isSuccess local audio");
                List<LocalAudio> savedAudios = ((ResultLocalAudios.Success)resultLocalAudios).getData();
                player.seekTo(savedAudios.get(0).getCurrentTime());
                player.play();
            }
        });

        setBarsBaseOnRotation();

    }
    /**
     *  check if at start the device is in landscape to enter fullscreen mode
     */
    private void setBarsBaseOnRotation() {
        Display display = ((WindowManager) getActivity().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        Log.d(TAG, "setBarsBaseOnRotation: " + orientation);
        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            enterLayoutFullScreenLandscape();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: ");
        int newOrientation = newConfig.orientation;
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterLayoutFullScreenLandscape();
        }else{
            exitLayoutFullScreenLandscape();
        }
    }

    private void enterLayoutFullScreenLandscape() {
        Log.d(TAG, "onConfigurationChanged: landscape");
        //youTubePlayerView.enterFullScreen();
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

    private void exitLayoutFullScreenLandscape() {
        //youTubePlayerView.exitFullScreen();
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
    public void onPause() {
        //todo save current time in database
        exitLayoutFullScreenLandscape();
        localAudio.setCurrentTime(player.getCurrentPosition());
        viewModelMedia.updateLocalAudio(localAudio);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }
}