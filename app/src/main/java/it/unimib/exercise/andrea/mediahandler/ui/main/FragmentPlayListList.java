package it.unimib.exercise.andrea.mediahandler.ui.main;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.DIVIDER_INSET;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_PLAYLIST_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterPlaylistsListRecView;
import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.repository.playlist.IPlaylistRepositoryWithLiveData;
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
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.top_app_bar, menu);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_main);
                NavController navController = navHostFragment.getNavController();

                if(navController.getCurrentDestination().getId() == R.id.fragment_playlist_list){
                    if (menuItem.getItemId() == R.id.topAppBarRefresh){
                        viewModelPlaylist.getPlaylistList(0);
                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                getString(R.string.updating), Snackbar.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                //NavController navController = Navigation.findNavController(view);
                NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_main);
                NavController navController = navHostFragment.getNavController();
                if(navController.getCurrentDestination().getId() == R.id.fragment_playlist_list){
                    MenuProvider.super.onPrepareMenu(menu);
                    MenuItem item = menu.findItem(R.id.topAppBarDelete);
                    item.setVisible(false);
                }

            }
        }, this.getViewLifecycleOwner());  // <--- carefull if Lyfecycle is omitted it cause duplicated menus across multiple fragments

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
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        divider.setDividerInsetEnd(DIVIDER_INSET);
        divider.setLastItemDecorated(false);
        recyclerViewPlaylistList.addItemDecoration(divider);


        viewModelPlaylist.getPlaylistList(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), resultPlaylist -> {
            if (resultPlaylist.isSuccess()){
                //result should contain ALL playlist of the channel
                Log.d(TAG, "onViewCreated: resultPlaylist.isSuccess");
                int updateSize = this.playlistList.size();
                this.playlistList.clear();
                this.playlistList.addAll(((Result.ResultPlaylistSuccess) resultPlaylist).getData().getPlaylistList());
                //if nothing has been deleted
                if(this.playlistList.size() >= updateSize){
                    updateSize = this.playlistList.size();
                }
                adapterPlaylistsListRecView.notifyItemRangeChanged(0, updateSize);
            }else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((
                        Result.Error)resultPlaylist).getMessage()),
                        Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "observe: not success");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().closeContextMenu();
    }
}