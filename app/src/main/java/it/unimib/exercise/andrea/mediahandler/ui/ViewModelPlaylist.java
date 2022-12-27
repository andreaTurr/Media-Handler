package it.unimib.exercise.andrea.mediahandler.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;

public class ViewModelPlaylist extends ViewModel {
    private static final String TAG = ViewModelPlaylist.class.getSimpleName();
    private MutableLiveData<Result> playlistListLiveData;
    private MutableLiveData<ResultPlaylistItem> playlistLiveData;
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

    private void fetchPlaylistList(long LastUpdate) {
        Log.d(TAG, "fetchPlaylistList: ");
        playlistListLiveData = playlistRepositoryWithLiveData.fetchPlaylistList(LastUpdate);
    }

    public MutableLiveData<ResultPlaylistItem> getPlaylistFromId(long lastUpdate, String playlistId) {
        if (playlistLiveData == null){
            fetchPlaylistFromId(lastUpdate, playlistId);
        }
        return playlistLiveData;
    }

    private void fetchPlaylistFromId(long LastUpdate, String playlistId){
        playlistLiveData = playlistRepositoryWithLiveData.fetchPlaylist(LastUpdate, playlistId);
    }
}
