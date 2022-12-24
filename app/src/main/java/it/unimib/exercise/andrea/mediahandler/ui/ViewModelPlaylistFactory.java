package it.unimib.exercise.andrea.mediahandler.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;


/**
 * Custom ViewModelProvider to be able to have a custom constructor
 * for the PlaylistViewModel class.
 */
public class ViewModelPlaylistFactory implements ViewModelProvider.Factory {

    private IPlaylistRepositoryWithLiveData iPlaylistRepositoryWithLiveData;

    public ViewModelPlaylistFactory(IPlaylistRepositoryWithLiveData iPlaylistRepositoryWithLiveData) {
        this.iPlaylistRepositoryWithLiveData = iPlaylistRepositoryWithLiveData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelPlaylist(iPlaylistRepositoryWithLiveData) ;
    }
}
