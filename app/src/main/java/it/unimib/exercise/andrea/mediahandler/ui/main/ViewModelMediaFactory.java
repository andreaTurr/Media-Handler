package it.unimib.exercise.andrea.mediahandler.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.exercise.andrea.mediahandler.repository.media.IMediaRepository;

public class ViewModelMediaFactory implements ViewModelProvider.Factory{
    private IMediaRepository mediaRepository;

    public ViewModelMediaFactory(IMediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelMedia(mediaRepository) ;
    }
}
