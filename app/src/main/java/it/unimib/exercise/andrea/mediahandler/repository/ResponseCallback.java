package it.unimib.exercise.andrea.mediahandler.repository;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

/**
 * Interface to send data from Repositories that implement
 * IPlaylistRepository interface to Activity/Fragment.
 */
public interface ResponseCallback {
    void onSuccess(List<Playlist> newsList, long lastUpdate);
    void onFailure(String errorMessage);
    void onNewsFavoriteStatusChanged(Playlist playlist);
}
