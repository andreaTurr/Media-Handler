package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.ResultPlaylist;
import it.unimib.exercise.andrea.mediahandler.models.video.ResultVideoDuration;

public interface IPlaylistRepositoryWithLiveData {
    MutableLiveData<ResultPlaylist> fetchPlaylistList(long lastUpdate);
    MutableLiveData<ResultPlaylistItem> fetchVideoList(String playlistId);
    MutableLiveData<ResultVideo> fetchVideo(String videoIdInPlaylist);
    void updateVideo(Video video);
    MutableLiveData<ResultVideoDuration> fetchTotalPlaylistDuration(String playlistId);
}
