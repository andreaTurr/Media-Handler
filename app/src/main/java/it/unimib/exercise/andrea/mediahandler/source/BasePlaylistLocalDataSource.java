package it.unimib.exercise.andrea.mediahandler.source;


import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

/**
 * Base class to get news from a local source.
 */
public abstract class BasePlaylistLocalDataSource {

    protected PlaylistCallback playlistCallback;

    public void setPlaylistCallback(PlaylistCallback playlistCallback) {
        this.playlistCallback = playlistCallback;
    }

    public abstract void getPlaylistList();
    public abstract void insertPlaylistsList(PlaylistApiResponse playlistList);

    public abstract void getPlaylist(String playlistId);
    public abstract void insertPlaylists(PlaylistApiResponse playlistList);
}
