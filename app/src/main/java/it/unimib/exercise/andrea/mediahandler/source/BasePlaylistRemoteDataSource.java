package it.unimib.exercise.andrea.mediahandler.source;

/**
 * Base class to get playlist from a remote source.
 */
public abstract class BasePlaylistRemoteDataSource {
    protected PlaylistCallback newsCallback;

    public void setNewsCallback(PlaylistCallback newsCallback) {this.newsCallback = newsCallback;}

    public abstract void getPlaylist(String user);
}
