package it.unimib.exercise.andrea.mediahandler.ui;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_PLAYLIST_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

import java.util.ArrayList;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterPlaylistsListRecView;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Result;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;
import it.unimib.exercise.andrea.mediahandler.util.ErrorMessagesUtil;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPlayListList extends Fragment {
    private List<Playlist> playlistList;
    private AdapterPlaylistsListRecView adapterPlaylistsListRecView;
    private ViewModelPlaylist viewModelPlaylist;
    private AuthStateManager mStateManager = null;
    private static final String TAG = FragmentPlayListList.class.getSimpleName();
    private SharedPreferencesUtil sharedPreferencesUtil;

    public FragmentPlayListList() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Log.d(TAG, "onCreate: start");
        AuthorizationResponse resp = AuthorizationResponse.fromIntent(getActivity().getIntent());
        AuthorizationException ex = AuthorizationException.fromIntent(getActivity().getIntent());
        if (resp != null){
            Log.d(TAG, "onActivityResult: save mAuth");
            mStateManager.updateAfterAuthorization(resp, ex);
        }*/
        IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData =
                ServiceLocator.getInstance().getPlaylistRepository(
                        requireActivity().getApplication(),
                        requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );
        if (playlistRepositoryWithLiveData != null) {
            // This is the way to create a ViewModel with custom parameters
            // (see NewsViewModelFactory class for the implementation details)
            viewModelPlaylist = new ViewModelProvider(
                    requireActivity(),
                    new ViewModelPlaylistFactory(playlistRepositoryWithLiveData)).get(ViewModelPlaylist.class);
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.unexpected_error), Snackbar.LENGTH_SHORT).show();
        }


        playlistList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });*/

        RecyclerView recyclerViewPlaylistList = view.findViewById(R.id.recyclerview_playlist_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapterPlaylistsListRecView = new AdapterPlaylistsListRecView(playlistList,
                requireActivity().getApplication(),
                new AdapterPlaylistsListRecView.OnItemClickListener(){
                    @Override
                    public void onPlaylistClick(Playlist playlist) {
                        Snackbar.make(view, playlist.getSnippet().getTitle(), Snackbar.LENGTH_SHORT).show();
                        //Navigation.findNavController(requireView()).navigate(R.id.action_fragment_playlist_list_to_fragmentPlaylist);

                        // method to pass argument between fragments of navigation components
                        // https://developer.android.com/guide/navigation/navigation-pass-data#samples
                        Log.d(TAG, "onPlaylistClick playlistId: " + playlist.getId());
                        FragmentPlayListListDirections.ActionFragmentPlaylistListToFragmentPlaylist action =
                                FragmentPlayListListDirections.actionFragmentPlaylistListToFragmentPlaylist(
                                        playlist.getId(),
                                        playlist.getSnippet().getTitle());
                        //action.setPlaylistId(playlist.getId());
                        Navigation.findNavController(view).navigate(action);
                    }
                });
        recyclerViewPlaylistList.setLayoutManager(layoutManager);
        recyclerViewPlaylistList.setAdapter(adapterPlaylistsListRecView);
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        String lastUpdate = "0";
        if (sharedPreferencesUtil.readStringData(
                SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE_PLAYLIST_LIST) != null) {
            lastUpdate = sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE_PLAYLIST_LIST);
        }
        //divider between items of recycle view
        recyclerViewPlaylistList.addItemDecoration(new DividerItemDecoration(getContext(),
                layoutManager.getOrientation()));


        viewModelPlaylist.getPlaylistList(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()){
                Log.d(TAG, "onViewCreated: result.isSuccess");
                int initialSize = this.playlistList.size();
                this.playlistList.clear();
                this.playlistList.addAll(((Result.Success) result).getData().getPlaylistList());
                adapterPlaylistsListRecView.notifyItemRangeInserted(initialSize, this.playlistList.size());
                //progressBar.setVisibility(View.GONE);
            }else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((
                        Result.Error)result).getMessage()),
                        Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "observe: not success");
            }
        });
    }
}