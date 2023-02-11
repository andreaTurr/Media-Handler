package it.unimib.exercise.andrea.mediahandler.repository.playlist;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.source.playlist.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.PlaylistCallback;

public class PlaylistRepositoryWithLiveData implements IPlaylistRepositoryWithLiveData, PlaylistCallback {
    private final MutableLiveData<Result> playlistsListMutableLiveData;
    private MutableLiveData<Result> playlistItemMutableLiveData; //got rid of final because variable is used by multiple videoLists
    private final BasePlaylistRemoteDataSource playlistRemoteDataSource;
    private final BasePlaylistLocalDataSource playlistLocalDataSource;
    private MutableLiveData<Result> videoMutableLiveData;
    private MutableLiveData<Result> videoDetailedMutableLiveData;

    private static final String TAG = PlaylistRepositoryWithLiveData.class.getSimpleName();
    public PlaylistRepositoryWithLiveData(BasePlaylistRemoteDataSource playlistRemoteDataSource,
                                          BasePlaylistLocalDataSource playlistLocalDataSource) {

        playlistsListMutableLiveData = new MutableLiveData<>();
        playlistItemMutableLiveData = new MutableLiveData<>();
        videoMutableLiveData = new MutableLiveData<>();
        videoDetailedMutableLiveData = new MutableLiveData<>();
        this.playlistRemoteDataSource = playlistRemoteDataSource;
        this.playlistLocalDataSource = playlistLocalDataSource;
        this.playlistRemoteDataSource.setPlaylistCallback(this);
        this.playlistLocalDataSource.setPlaylistCallback(this);
    }
    @Override
    public MutableLiveData<Result> fetchPlaylistList(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        //todo check if database empty, if so retrieve data from remote
        if(currentTime - lastUpdate > FRESH_TIMEOUT)
            playlistRemoteDataSource.getPlaylistList();
        else
            playlistLocalDataSource.getPlaylistList();
        return playlistsListMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> fetchVideoList(String playlistId, boolean refresh) {

        Log.d(TAG, "fetchVideoList: ");
        //used to get last update from server, logic continues in callback
        if (refresh == true){
            onSuccessFromLocalLastUpdate(0L, playlistId);
        }else{
            /*to prevent past value from being taken,
            only if no refresh in order to preserve observe on livedata*/
            playlistItemMutableLiveData = new MutableLiveData<>();
            playlistLocalDataSource.getPlaylistLastUpdate(playlistId);
        }

        return playlistItemMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> fetchVideo(String videoIdInPlaylist) {
        Log.d(TAG, "fetchVideo: ");
        if (videoMutableLiveData.getValue() != null){
            if(videoMutableLiveData.getValue().isSuccess()){
                if (((Result.ResultVideoSuccess)videoMutableLiveData.getValue()).getData()
                        .getContentDetails().getVideoId() != videoIdInPlaylist){
                    videoMutableLiveData = new MutableLiveData<>();  //to prevent past value from being taken
                }
            }
        }
        playlistLocalDataSource.getVideo(videoIdInPlaylist);
        return videoMutableLiveData;
    }

    @Override
    public void updateVideo(Video video) {
        playlistLocalDataSource.insertVideo(video);
    }

    @Override
    public MutableLiveData<Result> fetchTotalPlaylistDuration(String playlistId) {
        videoDetailedMutableLiveData = new MutableLiveData<>();
        playlistRemoteDataSource.getPlaylistDuration(playlistId);
        return videoDetailedMutableLiveData;
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
        playlistsListMutableLiveData.postValue(result);

    }

    @Override
    public void onSuccessFromLocalPlaylistList(PlaylistApiResponse playlistApiResponse) {
        Log.d(TAG, "onSuccessFromLocalPlaylistList: ");
        Result.ResultPlaylistSuccess result = new Result.ResultPlaylistSuccess(playlistApiResponse);
        playlistsListMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocalPlaylistList(Exception exception) {
        Log.d(TAG, "onFailureFromLocal: ");
        Result.Error resultError = new Result.Error(exception.getMessage());
        playlistsListMutableLiveData.postValue(resultError);
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
        Result.Error resultError = new Result.Error(exception.getMessage());
        playlistItemMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccessFromLocalVideoList(PlaylistItemApiResponse response) {
        Log.d(TAG, "onSuccessFromLocalVideoList: ");
        Result.ResultPlaylistItemSuccess result =
                new Result.ResultPlaylistItemSuccess(new PlaylistItemApiResponse(response.getVideoList()));
        playlistItemMutableLiveData.postValue(result);

    }

    /**
     * return error and tries to get VideoList from remote source
     * @param exception
     */
    @Override
    public void onFailureFromLocalVideoList(Exception exception) {
        Log.d(TAG, "onFailureFromLocalVideoList: ");
        Result.Error resultError = new Result.Error(exception.getMessage());
        playlistItemMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccessFromLocalVideo(Video video) {
        Result.ResultVideoSuccess result = new Result.ResultVideoSuccess(video);
        videoMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocalVideo(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        videoMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccesFromRemotePlaylistDuration(long duration) {
        Result.ResultVideoDurationSuccess result = new Result.ResultVideoDurationSuccess(duration);
        videoDetailedMutableLiveData.postValue(result);

    }

    @Override
    public void onSuccessDeletion() {

    }
}
