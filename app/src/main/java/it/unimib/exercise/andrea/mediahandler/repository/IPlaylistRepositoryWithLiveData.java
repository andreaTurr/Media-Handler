package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Result;

public interface IPlaylistRepositoryWithLiveData {
    MutableLiveData<Result> fetchPlaylist();
}
