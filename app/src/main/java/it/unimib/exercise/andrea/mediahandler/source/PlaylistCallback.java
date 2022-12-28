package it.unimib.exercise.andrea.mediahandler.source;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public interface PlaylistCallback {
    //Playlist
    void onSuccessFromRemotePlaylistList(PlaylistApiResponse newsApiResponse);
    void onFailureFromRemotePlaylistList(Exception exception);
    void onSuccessFromLocalPlaylistList(PlaylistApiResponse playlistApiResponse);
    void onFailureFromLocalPlaylistList(Exception exception);
    void onSuccessFromLocalLastUpdate(Long lastUpdate, String playlistId);
    //void onFailureFromLocalLastUpdate(String playlistId);

    //PlaylistItem
    void onSuccessFromRemoteVideoList(PlaylistItemApiResponse response, String playlistId) ;
    void onFailureFromRemoteVideoList(Exception exception);
    void onSuccessFromLocalPlaylistItem(PlaylistItemApiResponse playlistItemApiResponse);
    void onFailureFromLocalPlaylistItem(Exception exception);

}
