package it.unimib.exercise.andrea.mediahandler.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.exercise.andrea.mediahandler.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class fragment_playlist extends Fragment {
    private static final String TAG = fragment_playlist.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_playlist() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                // It adds the menu item in the toolbar
                menuInflater.inflate(R.menu.top_app_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.delete) {
                    //newsViewModel.deleteAllFavoriteNews();
                    Log.d(TAG, "Delete selected");
                }
                return false;
            }
            // Use getViewLifecycleOwner() to avoid that the listener
            // associated with a menu icon is called twice
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);


        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }
}