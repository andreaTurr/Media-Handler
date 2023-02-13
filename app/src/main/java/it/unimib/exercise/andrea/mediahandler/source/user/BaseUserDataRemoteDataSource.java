package it.unimib.exercise.andrea.mediahandler.source.user;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.User;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.repository.user.UserResponseCallback;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
    public abstract void getUserPlaylists(String idToken);
    public abstract void getUserVideos(String idToken);
    public abstract void getUserPreferences(String idToken);
    public abstract void saveUserPlaylists(List<Playlist> playlistList, String idToken);
    public abstract void saveUserVideos(List<Video> videoList, String idToken);
}
