package it.unimib.exercise.andrea.mediahandler.ui.main;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.SECONDS_IN_HOUR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SECONDS_IN_MINUTE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.repository.playlist.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPlaylistDetail extends Fragment {

    private Video[] videoArray;
    private ViewModelPlaylist viewModelPlaylist;
    private static final String TAG = FragmentPlaylistDetail.class.getSimpleName();
    private TextView playlistDuration;

    public FragmentPlaylistDetail() {
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
        return inflater.inflate(R.layout.fragment_playlist_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoArray = FragmentPlaylistDetailArgs.fromBundle(getArguments()).getVideoArray();
        String playlistId = FragmentPlaylistDetailArgs.fromBundle(getArguments()).getPlaylistId();
        String playlistTitle = FragmentPlaylistDetailArgs.fromBundle(getArguments()).getPlaylistTitle();
        Log.d(TAG, "onViewCreated: " + videoArray.toString());
        playlistDuration = view.findViewById(R.id.textViewPlaylistDuration);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(playlistTitle);

        String videoIds = "";

        for (Video video:
                videoArray) {
            videoIds += video.getContentDetails().getVideoId() + ",";
        }
        videoIds = videoIds.substring(0, videoIds.length() - 1);
        Log.d(TAG, "onViewCreated: " + videoIds);

        //ConstraintLayout details = view.findViewById(R.id.DetailsSubView);
        MaterialCardView materialCardView = view.findViewById(R.id.DetailsSubView);
        ProgressBar progressBar = view.findViewById(R.id.progressBarPlaylistDetails);

        materialCardView.setVisibility(View.GONE);

        viewModelPlaylist.getPlaylistDuration(videoIds).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()){
                Log.d(TAG, "onViewCreated: ");
                printTime(((Result.ResultVideoDurationSuccess)result).getData());
                materialCardView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void printTime(long totalSeconds){
        long hours = totalSeconds / SECONDS_IN_HOUR ;
        totalSeconds %= SECONDS_IN_HOUR;
        long minutes = totalSeconds / SECONDS_IN_MINUTE;
        totalSeconds %= SECONDS_IN_MINUTE;
        long seconds = totalSeconds;
        String result;
        if (hours != 0){
            result = getString(R.string.totalHours)   + ": " + String.format("%02dh %02dm %02ds", hours, minutes,seconds);
        }else if(minutes != 0){
            result = getString(R.string.totalMinutes) + ": " + String.format("%02dm %02ds", minutes,seconds);
        }else if(seconds != 0) {
            result = getString(R.string.totalSeconds) + ": " + String.format("%02ds", seconds);
        }else{
            result = getString(R.string.totalTimeError);
        }
        playlistDuration.setText(result);
    }

}