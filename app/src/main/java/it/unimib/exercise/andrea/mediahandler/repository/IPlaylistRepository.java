package it.unimib.exercise.andrea.mediahandler.repository;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;

public interface IPlaylistRepository {
    void fetchPlaylist(String username, int page, long lastUpdate);

    void updatePlaylist(Playlist news);

    //TODO aggiungere stato di avanzamento playlist
}
