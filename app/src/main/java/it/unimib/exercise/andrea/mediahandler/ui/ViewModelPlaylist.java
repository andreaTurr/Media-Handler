package it.unimib.exercise.andrea.mediahandler.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultPlaylistItem;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.ResultVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.ResultPlaylist;
import it.unimib.exercise.andrea.mediahandler.models.video.ResultVideoDuration;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;

public class ViewModelPlaylist extends ViewModel {
    private static final String TAG = ViewModelPlaylist.class.getSimpleName();
    private MutableLiveData<ResultPlaylist> playlistListLiveData;
    private MutableLiveData<ResultPlaylistItem> videoListLiveData;
    private MutableLiveData<ResultVideo> videoLiveData;
    private final IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData;
    private MutableLiveData<ResultVideoDuration> durationLiveData;

    public ViewModelPlaylist(IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData) {
        this.playlistRepositoryWithLiveData = playlistRepositoryWithLiveData;
    }

    public MutableLiveData<ResultPlaylist> getPlaylistList(long lastUpdate) {
        if (playlistListLiveData == null || lastUpdate == 0){
            fetchPlaylistList(lastUpdate);
        }
        return playlistListLiveData;
    }

    public MutableLiveData<ResultPlaylistItem> getPlaylistFromId(String playlistId, boolean refresh) {
        fetchPlaylistFromId(playlistId, refresh);
        return videoListLiveData;
    }

    public MutableLiveData<ResultVideo> getVideo(String videoIdInPlaylist ){
        if (videoLiveData == null){
            fetchVideo(videoIdInPlaylist);
        }
        return videoLiveData;
    }

    public MutableLiveData<ResultVideoDuration> getPlaylistDuration(String videoIds){
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
