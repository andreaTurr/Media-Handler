package it.unimib.exercise.andrea.mediahandler.source.playlist;


import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

/**
 * Base class to get news from a local source.
 */
public abstract class BasePlaylistLocalDataSource {

    protected PlaylistCallback playlistCallback;

    public void setPlaylistCallback(PlaylistCallback playlistCallback) {
        this.playlistCallback = playlistCallback;
    }
    //Playlist
    public abstract void getPlaylistList(int responseType);
    public abstract void insertPlaylistsList(PlaylistApiResponse playlistItemApiResponse, boolean setLastUpdate, int responseType);
    public abstract void getPlaylistLastUpdate(String playlistId);

    //Videolist
    public abstract void getVideoList(String playlistId);
    public abstract void insertVideoList(PlaylistItemApiResponse playlistItemApiResponse, String playlistId, boolean setUpdateTime, int responseType);
    public abstract void insertVideo(Video video);
    public abstract void getVideo(String videoId);

    public abstract void deleteAll();

    public abstract void getAllVideos();
}
