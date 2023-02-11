package it.unimib.exercise.andrea.mediahandler.ui.welcome;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.exercise.andrea.mediahandler.repository.user.IUserRepository;

/**
 * Custom ViewModelProvider to be able to have a custom constructor
 * for the UserViewModel class.
 */
public class ViewModelFactoryUser implements ViewModelProvider.Factory {

    private final IUserRepository userRepository;

    public ViewModelFactoryUser(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewModelUser(userRepository);
    }
}
