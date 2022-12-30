package it.unimib.exercise.andrea.mediahandler.ui;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_VIDEO_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterPlaylistRecView;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.util.ErrorMessagesUtil;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPlaylist extends Fragment {
    private static final String TAG = FragmentPlaylist.class.getSimpleName();
    private AdapterPlaylistRecView adapterPlaylistRecView;
    private List<Video> videoList;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ViewModelPlaylist viewModelPlaylist;

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
            // (see NewsViewModelFactory class for the implementation details)
            viewModelPlaylist = new ViewModelProvider(
                    requireActivity(),
                    new ViewModelPlaylistFactory(playlistRepositoryWithLiveData)).get(ViewModelPlaylist.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }

        videoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get value passed from originating action fragment
        String playlistId = FragmentPlaylistArgs.fromBundle(getArguments()).getPlaylistId();
        String playlistTitle = FragmentPlaylistArgs.fromBundle(getArguments()).getPlaylistTitle();

        Log.d(TAG, "onViewCreated: get playlist: " + playlistId);
        TextView titleOfPlaylist = view.findViewById(R.id.playlistTitle);
        titleOfPlaylist.setText(playlistTitle);

        RecyclerView recyclerViewPlaylistItems = view.findViewById(R.id.recyclerview_playlist);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapterPlaylistRecView = new AdapterPlaylistRecView(
                videoList,
                getActivity().getApplication(),
                new AdapterPlaylistRecView.OnItemClickListener() {
                    @Override
                    public void onVideoClick(Video video) {

                    }
                });
        recyclerViewPlaylistItems.setLayoutManager(layoutManager);
        recyclerViewPlaylistItems.setAdapter(adapterPlaylistRecView);
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        String lastUpdate = "0";
        if (sharedPreferencesUtil.readStringData(
                SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE_VIDEO_LIST) != null) {
            lastUpdate = sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE_VIDEO_LIST);
        }
        //divider between items of recycle view
        recyclerViewPlaylistItems.addItemDecoration(new DividerItemDecoration(getContext(),
                layoutManager.getOrientation()));


        viewModelPlaylist.getPlaylistFromId(Long.parseLong(lastUpdate), playlistId).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()){
                Log.d(TAG, "onViewCreated: isSuccess");
                int initialSize = this.videoList.size();
                //Log.d(TAG, "result.isSuccess: " + videoList);
                this.videoList.clear();
                //Log.d(TAG, "result.isSuccess: " + videoList);
                //this.videoList.addAll(((Result.Success) result).getData().getPlaylist());
                this.videoList.addAll(((ResultPlaylistItem.Success) result).getData().getVideoList());
                Log.d(TAG, "result.isSuccess: " + videoList);
                adapterPlaylistRecView.notifyItemRangeRemoved(0, initialSize);
                adapterPlaylistRecView.notifyItemRangeInserted(initialSize, this.videoList.size());
                //Log.d(TAG, "onViewCreated: " + videoList);
                //progressBar.setVisibility(View.GONE);
            }else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((
                                ResultPlaylistItem.Error)result).getMessage()),
                        Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "observe: not success");
            }
        } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapterPlaylistRecView.notifyItemRangeRemoved(0, videoList.size());
    }
}