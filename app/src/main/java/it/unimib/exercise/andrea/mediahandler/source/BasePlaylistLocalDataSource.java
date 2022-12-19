package it.unimib.exercise.andrea.mediahandler.source;


import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;

/**
 * Base class to get news from a local source.
 */
public abstract class BasePlaylistLocalDataSource {

    protected PlaylistCallback playlistCallback;

    public void setPlaylistCallback(PlaylistCallback playlistCallback) {
        this.playlistCallback = playlistCallback;
    }

    public abstract void getPlaylist();
    public void insertPlaylists(List<Playlist> playlistList) {}
}
