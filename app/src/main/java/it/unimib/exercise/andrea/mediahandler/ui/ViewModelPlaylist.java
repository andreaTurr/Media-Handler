package it.unimib.exercise.andrea.mediahandler.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.playlist.Result;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepository;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;

public class ViewModelPlaylist extends ViewModel {
    private static final String TAG = ViewModelPlaylist.class.getSimpleName();
    private MutableLiveData<Result> playlistListLiveData;
    private final IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData;

    public ViewModelPlaylist(IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData) {
        this.playlistRepositoryWithLiveData = playlistRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getPlaylistList() {
        if (playlistListLiveData == null){
            fetchPlaylistList();
        }
        return playlistListLiveData;
    }

    private void fetchPlaylistList() {
        playlistListLiveData = playlistRepositoryWithLiveData.fetchPlaylist();
    }
}
