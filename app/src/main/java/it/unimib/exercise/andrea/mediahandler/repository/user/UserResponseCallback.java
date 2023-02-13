package it.unimib.exercise.andrea.mediahandler.repository.user;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.User;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    void onSuccessFromRemoteDatabase(List<Playlist> playlistList);
    void onSuccessFromGettingUserPreferences();
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();

    void onSuccessFromPlaylistListSync(List<Playlist> playlistList, String idToken);
    void onSuccessFromVideoListSync(List<Video> videoList);
}
