package it.unimib.exercise.andrea.mediahandler.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;

public class ViewModelPlaylist extends ViewModel {
    private static final String TAG = ViewModelPlaylist.class.getSimpleName();
    private MutableLiveData<Result> playlistListLiveData;
    private MutableLiveData<ResultPlaylistItem> videoListLiveData;
    private MutableLiveData<ResultVideo> videoLiveData;
    private final IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData;

    public ViewModelPlaylist(IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData) {
        this.playlistRepositoryWithLiveData = playlistRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getPlaylistList(long lastUpdate) {
        if (playlistListLiveData == null){
            fetchPlaylistList(lastUpdate);
        }
        return playlistListLiveData;
    }

    public MutableLiveData<ResultPlaylistItem> getPlaylistFromId(String playlistId) {
        fetchPlaylistFromId(playlistId);
        return videoListLiveData;
    }

    public MutableLiveData<ResultVideo> getVideo(String videoIdInPlaylist ){
        if (videoLiveData == null){
            fetchVideo(videoIdInPlaylist);
        }
        return videoLiveData;
    }

    private void fetchVideo(String videoIdInPlaylist) {
        videoLiveData = playlistRepositoryWithLiveData.fetchVideo(videoIdInPlaylist);
    }

    private void fetchPlaylistList(long LastUpdate) {
        Log.d(TAG, "fetchPlaylistList: ");
        playlistListLiveData = playlistRepositoryWithLiveData.fetchPlaylistList(LastUpdate);
    }

    private void fetchPlaylistFromId(String playlistId){
        videoListLiveData = playlistRepositoryWithLiveData.fetchVideoList(playlistId);
    }

    public void updateVideo(Video video){
        playlistRepositoryWithLiveData.updateVideo(video);
    }



}
