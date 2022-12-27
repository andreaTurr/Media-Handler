package it.unimib.exercise.andrea.mediahandler.repository;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistCallback;

public class PlaylistRepositoryWithLiveData implements IPlaylistRepositoryWithLiveData, PlaylistCallback {
    private final MutableLiveData<Result> allPlaylistsListMutableLiveData;
    private final MutableLiveData<ResultPlaylistItem> allPlaylistItemMutableLiveData;
    private final BasePlaylistRemoteDataSource playlistRemoteDataSource;
    private final BasePlaylistLocalDataSource playlistLocalDataSource;

    private static final String TAG = PlaylistRepositoryWithLiveData.class.getSimpleName();
    public PlaylistRepositoryWithLiveData(BasePlaylistRemoteDataSource playlistRemoteDataSource,
                                          BasePlaylistLocalDataSource playlistLocalDataSource) {

        allPlaylistsListMutableLiveData = new MutableLiveData<>();
        allPlaylistItemMutableLiveData = new MutableLiveData<>();
        this.playlistRemoteDataSource = playlistRemoteDataSource;
        this.playlistLocalDataSource = playlistLocalDataSource;
        this.playlistRemoteDataSource.setPlaylistCallback(this);
        this.playlistLocalDataSource.setPlaylistCallback(this);
    }
    @Override
    public MutableLiveData<Result> fetchPlaylistList(long lastUpdate) {
        long currentTime = System.currentTimeMillis();
        Log.d(TAG, "fetchPlaylist: ");
        if(currentTime - lastUpdate > FRESH_TIMEOUT)
            playlistRemoteDataSource.getPlaylistList();
        else
            playlistLocalDataSource.getPlaylistList();
        return allPlaylistsListMutableLiveData;
    }

    @Override
    public MutableLiveData<ResultPlaylistItem> fetchPlaylist(long lastUpdate, String playlistId) {
        long currentTime = System.currentTimeMillis();
        Log.d(TAG, "fetchPlaylist: ");
        //if(currentTime - lastUpdate > FRESH_TIMEOUT)
            playlistRemoteDataSource.getPlaylist(playlistId);
        //else
        //    playlistLocalDataSource.getPlaylist(playlistId);
        return allPlaylistItemMutableLiveData;
    }

    //PlaylistList callback
    @Override
    public void onSuccessFromRemotePlaylistList(PlaylistApiResponse playlistApiResponse) {
        Log.d(TAG, "onSuccessFromRemote: ");
        playlistLocalDataSource.insertPlaylistsList(playlistApiResponse);
    }

    @Override
    public void onFailureFromRemotePlaylistList(Exception exception) {
        Log.d(TAG, "onFailureFromRemotePlaylistList: ");
        Result.Error result = new Result.Error(exception.getMessage());
        allPlaylistsListMutableLiveData.postValue(result);
    }

    public void onSuccessFromLocalPlaylistList(List<Playlist> playlistList) {
        Log.d(TAG, "onSuccessFromLocalPlaylistList: ");
        Result.Success result = new Result.Success(new PlaylistApiResponse(playlistList));
        allPlaylistsListMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocalPlaylistList(Exception exception) {
        Log.d(TAG, "onFailureFromLocal: ");
        Result.Error resultError = new Result.Error(exception.getMessage());
        allPlaylistsListMutableLiveData.postValue(resultError);
    }

    //PlaylistItem callback
    @Override
    public void onSuccessFromRemotePlaylistItem(PlaylistItemApiResponse response) {
        Log.d(TAG, "onSuccessFromRemotePlaylistItem: ");
        //Result.Success result = new Result.Success(new PlaylistApiResponse(playlistList));
        ResultPlaylistItem.Success result =
                new ResultPlaylistItem.Success(new PlaylistItemApiResponse(response.getVideoList()));
        allPlaylistItemMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemotePlaylistItem(Exception exception) {

    }
}
