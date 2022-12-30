package it.unimib.exercise.andrea.mediahandler.repository;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistCallback;

public class PlaylistRepositoryWithLiveData implements IPlaylistRepositoryWithLiveData, PlaylistCallback {
    private final MutableLiveData<Result> allPlaylistsListMutableLiveData;
    private MutableLiveData<ResultPlaylistItem> allPlaylistItemMutableLiveData; //got rid of final because variable is used by multiple videoLists
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

        //todo check if database empty, if so the retrieve data from remote
        if(currentTime - lastUpdate > FRESH_TIMEOUT)
            playlistRemoteDataSource.getPlaylistList();
        else
            playlistLocalDataSource.getPlaylistList();
        return allPlaylistsListMutableLiveData;
    }

    @Override
    public MutableLiveData<ResultPlaylistItem> fetchVideoList(long lastUpdate, String playlistId) {
        allPlaylistItemMutableLiveData = new MutableLiveData<>();  //to prevent past value from being taken
        Log.d(TAG, "fetchVideoList: ");
        //used to get last update from server, logic continues in callback
        playlistLocalDataSource.getPlaylistLastUpdate(playlistId);
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

    @Override
    public void onSuccessFromLocalPlaylistList(PlaylistApiResponse playlistApiResponse) {
        Log.d(TAG, "onSuccessFromLocalPlaylistList: ");
        Result.Success result = new Result.Success(playlistApiResponse);
        allPlaylistsListMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocalPlaylistList(Exception exception) {
        Log.d(TAG, "onFailureFromLocal: ");
        Result.Error resultError = new Result.Error(exception.getMessage());
        allPlaylistsListMutableLiveData.postValue(resultError);
    }

    // getting last update to check if needs to update or not base on time delta
    @Override
    public void onSuccessFromLocalLastUpdate(Long lastUpdate, String playlistId) {
        Log.d(TAG, "onSuccessFromLocalLastUpdate: last update of playlist: " + lastUpdate);
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastUpdate > FRESH_TIMEOUT)
            playlistRemoteDataSource.getVideoList(playlistId);
        else
            playlistLocalDataSource.getVideoList(playlistId);
    }

    //PlaylistItem callback
    @Override
    public void onSuccessFromRemoteVideoList(PlaylistItemApiResponse response, String playlistId) {
        Log.d(TAG, "onSuccessFromRemoteVideoList: ");
        //Result.Success result = new Result.Success(new PlaylistApiResponse(playlistList));
        playlistLocalDataSource.insertVideoList(response, playlistId);
    }

    @Override
    public void onFailureFromRemoteVideoList(Exception exception) {
        Log.d(TAG, "onFailureFromRemoteVideoList: ");
        ResultPlaylistItem.Error resultError = new ResultPlaylistItem.Error(exception.getMessage());
        allPlaylistItemMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccessFromLocalVideoList(PlaylistItemApiResponse response) {
        Log.d(TAG, "onSuccessFromLocalVideoList: ");
        ResultPlaylistItem.Success result =
                new ResultPlaylistItem.Success(new PlaylistItemApiResponse(response.getVideoList()));
        allPlaylistItemMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocalVideoList(Exception exception) {
        Log.d(TAG, "onFailureFromLocalVideoList: ");
        ResultPlaylistItem.Error resultError = new ResultPlaylistItem.Error(exception.getMessage());
        allPlaylistItemMutableLiveData.postValue(resultError);
    }
}
