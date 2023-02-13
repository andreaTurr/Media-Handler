package it.unimib.exercise.andrea.mediahandler.source.playlist;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public interface PlaylistCallback {
    //Playlist
    void onSuccessFromRemotePlaylistList(PlaylistApiResponse newsApiResponse);
    void onFailureFromRemotePlaylistList(Exception exception);
    void onSuccessFromLocalPlaylistList(PlaylistApiResponse playlistApiResponse, int type);
    void onFailureFromLocalPlaylistList(Exception exception);
    void onSuccessFromLocalLastUpdate(Long lastUpdate, String playlistId);
    //void onFailureFromLocalLastUpdate(String playlistId);

    //PlaylistItem
    void onSuccessFromRemoteVideoList(PlaylistItemApiResponse response, String playlistId) ;
    void onFailureFromRemoteVideoList(Exception exception);
    void onSuccessFromLocalVideoList(PlaylistItemApiResponse playlistItemApiResponse, int type);
    void onFailureFromLocalVideoList(Exception exception);

    //Video
    void onSuccessFromLocalVideo(Video video);
    void onFailureFromLocalVideo(Exception exception);
    void onSuccesFromRemotePlaylistDuration(long duration);

    void onSuccessDeletion();
}
