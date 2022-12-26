package it.unimib.exercise.andrea.mediahandler.source;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public interface PlaylistCallback {
    void onSuccessFromRemote(PlaylistApiResponse newsApiResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Playlist> newsList);
    void onFailureFromLocal(Exception exception);
}
