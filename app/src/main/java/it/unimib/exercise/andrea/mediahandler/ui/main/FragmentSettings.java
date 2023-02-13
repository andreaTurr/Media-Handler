package it.unimib.exercise.andrea.mediahandler.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import net.openid.appauth.AuthState;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.databinding.FragmentSettingsBinding;
import it.unimib.exercise.andrea.mediahandler.repository.user.IUserRepository;
import it.unimib.exercise.andrea.mediahandler.ui.welcome.ViewModelFactoryUser;
import it.unimib.exercise.andrea.mediahandler.ui.welcome.ViewModelUser;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentSettings extends Fragment {
    private static final String TAG = FragmentSettings.class.getSimpleName();
    private ViewModelUser userViewModel;
    private FragmentSettingsBinding fragmentSettingsBinding;
    private AuthStateManager mStateManager = null;

    public FragmentSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new ViewModelFactoryUser(userRepository)).get(ViewModelUser.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return fragmentSettingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        fragmentSettingsBinding.buttonLogout.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            if (activity != null){
                mStateManager = AuthStateManager.getInstance(activity) ;
                mStateManager.replace(new AuthState());
                userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                    Log.d(TAG, "onViewCreated: result logout" );
                    if (result.isSuccess()) {
                        Log.d(TAG, "onViewCreated: success logout");
                        Navigation.findNavController(view).navigate(
                                R.id.action_fragment_settings_to_activityWelcome);
                    } else {
                        Snackbar.make(view,
                                requireActivity().getString(R.string.unexpected_error),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

        });

        fragmentSettingsBinding.buttonSync.setOnClickListener(view1 -> userViewModel.saveYTData().observe(getViewLifecycleOwner(), result -> {

            if (result.isSuccess()) {
                Log.d(TAG, "savePlaylistList: result success" );
                Snackbar.make(view,
                        requireActivity().getString(R.string.sync_success_upl),
                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "savePlaylistList: result error" );
                Snackbar.make(view,
                        requireActivity().getString(R.string.sync_error_upl),
                        Snackbar.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentSettingsBinding = null;
    }
}