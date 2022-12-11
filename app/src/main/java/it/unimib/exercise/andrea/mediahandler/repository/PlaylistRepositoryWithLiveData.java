package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Result;

public interface PlaylistRepositoryWithLiveData {
    MutableLiveData<Result> fetchNews(String country, int page, long lastUpdate);

    MutableLiveData<Result> getFavoriteNews();

    void updatePlaylist(Playlist news);

    void deleteFavoriteNews();
}
