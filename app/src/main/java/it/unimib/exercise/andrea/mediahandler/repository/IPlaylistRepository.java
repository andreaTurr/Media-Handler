package it.unimib.exercise.andrea.mediahandler.repository;

import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

public interface IPlaylistRepository {
    void fetchPlaylist();
    void updatePlaylist(Playlist news);

    //TODO aggiungere stato di avanzamento playlist
}
