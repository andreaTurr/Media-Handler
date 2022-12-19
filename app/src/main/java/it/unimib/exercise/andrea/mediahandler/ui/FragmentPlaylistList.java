package it.unimib.exercise.andrea.mediahandler.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterPlaylistRecView;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Result;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.util.ErrorMessagesUtil;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentPlaylistList extends Fragment {
    private List<Playlist> playlistList;
    private AdapterPlaylistRecView adapterPlaylistRecView;
    private ViewModelPlaylist viewModelPlaylist;

    public FragmentPlaylistList() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IPlaylistRepositoryWithLiveData playlistRepositoryWithLiveData =
                ServiceLocator.getInstance().getNewsRepository(
                        requireActivity().getApplication(),
                        requireActivity().getApplication().getResources().getBoolean(R.bool.debug_mode)
                );
        viewModelPlaylist = new ViewModelProvider(
                requireActivity(),
                new ViewModelPlaylistFactory(playlistRepositoryWithLiveData)).get(ViewModelPlaylist.class);

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
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        RecyclerView recyclerViewCountryNews = view.findViewById(R.id.recyclerview_playlist_list);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapterPlaylistRecView = new AdapterPlaylistRecView(playlistList,
                requireActivity().getApplication(),
                new AdapterPlaylistRecView.OnItemClickListener(){

                    @Override
                    public void onPlaylistClick(Playlist playlist) {

                    }
                });
        recyclerViewCountryNews.setLayoutManager(layoutManager);
        recyclerViewCountryNews.setAdapter(adapterPlaylistRecView);

        viewModelPlaylist.getPlaylistList().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()){
                int initialSize = this.playlistList.size();
                this.playlistList.clear();
                this.playlistList.addAll(((Result.Success) result).getData().getPlaylistList());
                adapterPlaylistRecView.notifyItemRangeInserted(initialSize, this.playlistList.size());
                //progressBar.setVisibility(View.GONE);
            }else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.
                                getErrorMessage(((Result.Error)result).getMessage()),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}