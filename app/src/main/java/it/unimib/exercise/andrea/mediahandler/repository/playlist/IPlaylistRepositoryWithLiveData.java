package it.unimib.exercise.andrea.mediahandler.repository.playlist;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;

public interface IPlaylistRepositoryWithLiveData {
    MutableLiveData<Result> fetchPlaylistList(long lastUpdate);
    MutableLiveData<Result> fetchVideoList(String playlistId, boolean refresh);
    MutableLiveData<Result> fetchVideo(String videoIdInPlaylist);
    void updateVideo(Video video);
    MutableLiveData<Result> fetchTotalPlaylistDuration(String playlistId);
}
