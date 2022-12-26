package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;

public interface IPlaylistRepositoryWithLiveData {
    MutableLiveData<Result> fetchPlaylist(long lastUpdate);
}
