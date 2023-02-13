package it.unimib.exercise.andrea.mediahandler.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.User;
import it.unimib.exercise.andrea.mediahandler.repository.user.IUserRepository;
import it.unimib.exercise.andrea.mediahandler.ui.main.ActivityMain;
import it.unimib.exercise.andrea.mediahandler.ui.main.FragmentPlaylistDetailArgs;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLoading extends Fragment {
    private static final String TAG = FragmentLoading.class.getSimpleName();
    private LinearProgressIndicator progressIndicator;
    private ViewModelUser userViewModel;


    public FragmentLoading() {
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
        if(!isNetworkConnected()){//have internet
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLoading_to_activityMain);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String idToken = FragmentLoadingArgs.fromBundle(getArguments()).getIdToken();
        progressIndicator = view.findViewById(R.id.loading);
        retrieveUserInformationAndStartActivity(idToken);
    }

    private void retrieveUserInformationAndStartActivity(String idToken) {
        progressIndicator.setVisibility(View.VISIBLE);
        userViewModel.updateLocalData(idToken).observe(
                getViewLifecycleOwner(), result -> {
                    Log.d(TAG, "updateLocalData: ");
                    if (result.isSuccess() ){
                        Log.d(TAG, "updateLocalData: result success");
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.sync_success_dwn),
                                Snackbar.LENGTH_SHORT).show();
                    }else{
                        Log.d(TAG, "updateLocalData: result error");
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                requireActivity().getString(R.string.sync_error_dwn),
                                Snackbar.LENGTH_SHORT).show();
                    }
                    progressIndicator.setVisibility(View.GONE);
                    Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLoading_to_activityMain);
                }
        );
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}