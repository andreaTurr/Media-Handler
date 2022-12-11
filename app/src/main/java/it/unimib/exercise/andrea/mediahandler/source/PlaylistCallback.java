package it.unimib.exercise.andrea.mediahandler.source;

import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;

public interface PlaylistCallback {
    void onSuccessFromRemote(PlaylistApiResponse newsApiResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
}
