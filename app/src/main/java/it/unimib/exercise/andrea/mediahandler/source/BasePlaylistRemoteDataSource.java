package it.unimib.exercise.andrea.mediahandler.source;

/**
 * Base class to get playlist from a remote source.
 */
public abstract class BasePlaylistRemoteDataSource {
    protected PlaylistCallback playlistCallback;

    public void setPlaylistCallback(PlaylistCallback newsCallback) {this.playlistCallback = newsCallback;}

    public abstract void getPlaylistList();

    public abstract void getPlaylist(String playlistId);
}
