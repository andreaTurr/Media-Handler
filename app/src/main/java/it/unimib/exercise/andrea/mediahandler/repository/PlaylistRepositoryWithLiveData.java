package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Result;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistCallback;

public class PlaylistRepositoryWithLiveData implements IPlaylistRepositoryWithLiveData, PlaylistCallback {
    private final MutableLiveData<Result> allPlaylistsMutableLiveData;
    private final BasePlaylistRemoteDataSource playlistRemoteDataSource;
    private final BasePlaylistLocalDataSource playlistLocalDataSource;
    public PlaylistRepositoryWithLiveData(BasePlaylistRemoteDataSource playlistRemoteDataSource,
                                          BasePlaylistLocalDataSource playlistLocalDataSource) {

        allPlaylistsMutableLiveData = new MutableLiveData<>();
        this.playlistRemoteDataSource = playlistRemoteDataSource;
        this.playlistLocalDataSource = playlistLocalDataSource;
        this.playlistRemoteDataSource.setPlaylistCallback(this);
        this.playlistLocalDataSource.setPlaylistCallback(this);
    }
    @Override
    public MutableLiveData<Result> fetchPlaylist() {
        if (allPlaylistsMutableLiveData == null){
            playlistRemoteDataSource.getPlaylist();
        }else{
            playlistLocalDataSource.getPlaylist();
        }
        return allPlaylistsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(PlaylistApiResponse playlistApiResponse) {
        playlistLocalDataSource.insertPlaylists(playlistApiResponse.getPlaylistList());
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allPlaylistsMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(List<Playlist> playlistList) {
        Result.Success result = new Result.Success(new PlaylistApiResponse(playlistList));
        allPlaylistsMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allPlaylistsMutableLiveData.postValue(resultError);
    }
}
