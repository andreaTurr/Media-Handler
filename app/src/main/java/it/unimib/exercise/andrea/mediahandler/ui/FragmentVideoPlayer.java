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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentVideoPlayer extends Fragment{
    private YouTubePlayerView youTubePlayerView;
    private MaterialToolbar topAppbar;
    private BottomNavigationView bottomNav;
    private static final String TAG = FragmentVideoPlayer.class.getSimpleName();
    private ViewModelPlaylist viewModelPlaylist;
    private Video video;
    private int videoPosition;
    YouTubePlayerTracker tracker;

    public FragmentVideoPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData =
                ServiceLocator.getInstance().getPlaylistRepository(
                        requireActivity().getApplication(),
                        requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );
        if (playlistRepositoryWithLiveData != null) {
            // This is the way to create a ViewModel with custom parameters
            // (see ViewModelPlaylistFactory class for the implementation details)
            viewModelPlaylist = new ViewModelProvider(
                    requireActivity(),
                    new ViewModelPlaylistFactory(playlistRepositoryWithLiveData)).get(ViewModelPlaylist.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }
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

        video = FragmentVideoPlayerArgs.fromBundle(getArguments()).getVideo();
        videoPosition = FragmentVideoPlayerArgs.fromBundle(getArguments()).getPsition();

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        tracker = new YouTubePlayerTracker();
        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            // Called when the player is ready to play videos.
            // You should start using the player only after this method is called.
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // using pre-made custom ui
                //DefaultPlayerUiController defaultPlayerUiController = new DefaultPlayerUiController(youTubePlayerView, youTubePlayer);
                //youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.getRootView());
                Snackbar.make(view, video.getSnippet().getTitle(), Snackbar.LENGTH_SHORT).show();
                youTubePlayer.loadVideo(video.getContentDetails().getVideoId(), video.getCurrentSecond());
            }
            // Called when the total duration of the video is loaded.
            // Note that getDuration() will return 0 until the video's metadata is loaded, which normally happens just after the video starts playing.
            @Override
            public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration){
                youTubePlayer.addListener(tracker);
                if (video.getVideoDuration() == 0){
                    video.setVideoDuration(duration);
                    viewModelPlaylist.updateVideo(video);
                }
            }
        });
        //IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        //youTubePlayerView.initialize(listener, options);

        setBarsBaseOnRotation();

        /*viewModelPlaylist.getVideo(video.getIdVideoInPlaylist()).observe(getViewLifecycleOwner(), resultVideo -> {
            if(resultVideo.isSuccess()){
                video = ((ResultVideo.Success) resultVideo).getData();
                Log.d(TAG, "onViewCreated: " + video);

            }
        });*/
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
        youTubePlayerView.enterFullScreen();
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
        youTubePlayerView.exitFullScreen();
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
        Log.d(TAG, "onPause: ");
        exitLayoutFullScreenLandscape();
        video.setCurrentSecond(tracker.getCurrentSecond());
        viewModelPlaylist.updateVideo(video);
        super.onPause();
    }
}