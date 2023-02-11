package it.unimib.exercise.andrea.mediahandler.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.repository.playlist.IPlaylistRepositoryWithLiveData;

public class ViewModelPlaylist extends ViewModel {
    private static final String TAG = ViewModelPlaylist.class.getSimpleName();
    private MutableLiveData<Result> playlistListLiveData;
    private MutableLiveData<Result> videoListLiveData;
    private MutableLiveData<Result> videoLiveData;
    private final IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData;
    private MutableLiveData<Result> durationLiveData;
    //private MutableLiveData<Result> resultLiveData;

    public ViewModelPlaylist(IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData) {
        this.playlistRepositoryWithLiveData = playlistRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getPlaylistList(long lastUpdate) {
        if (playlistListLiveData == null || lastUpdate == 0){
            fetchPlaylistList(lastUpdate);
        }
        return playlistListLiveData;
    }

    public MutableLiveData<Result> getPlaylistFromId(String playlistId, boolean refresh) {
        fetchPlaylistFromId(playlistId, refresh);
        return videoListLiveData;
    }

    public MutableLiveData<Result> getVideo(String videoIdInPlaylist ){
        if (videoLiveData == null){
            fetchVideo(videoIdInPlaylist);
        }
        return videoLiveData;
    }

    public MutableLiveData<Result> getPlaylistDuration(String videoIds){
        fetchPlaylistDuration(videoIds);
        return durationLiveData;
    }



    private void fetchVideo(String videoIdInPlaylist) {
        videoLiveData = playlistRepositoryWithLiveData.fetchVideo(videoIdInPlaylist);
    }

    private void fetchPlaylistList(long LastUpdate) {
        Log.d(TAG, "fetchPlaylistList: ");
        playlistListLiveData = playlistRepositoryWithLiveData.fetchPlaylistList(LastUpdate);
    }

    private void fetchPlaylistFromId(String playlistId, boolean refresh){
        videoListLiveData = playlistRepositoryWithLiveData.fetchVideoList(playlistId, refresh);
    }

    public void updateVideo(Video video){
        playlistRepositoryWithLiveData.updateVideo(video);
    }

    private void fetchPlaylistDuration(String videoIds) {
        durationLiveData = playlistRepositoryWithLiveData.fetchTotalPlaylistDuration(videoIds);
    }



}
