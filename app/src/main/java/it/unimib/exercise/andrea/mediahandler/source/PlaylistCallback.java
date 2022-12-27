package it.unimib.exercise.andrea.mediahandler.source;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public interface PlaylistCallback {
    void onSuccessFromRemotePlaylistList(PlaylistApiResponse newsApiResponse);
    void onFailureFromRemotePlaylistList(Exception exception);
    void onSuccessFromLocalPlaylistList(List<Playlist> newsList);
    void onFailureFromLocalPlaylistList(Exception exception);

    void onSuccessFromRemotePlaylistItem(PlaylistItemApiResponse response) ;

    void onFailureFromRemotePlaylistItem(Exception exception);

}
