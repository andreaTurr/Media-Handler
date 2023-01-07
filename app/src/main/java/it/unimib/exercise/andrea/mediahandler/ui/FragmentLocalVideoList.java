package it.unimib.exercise.andrea.mediahandler.ui;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.DIVIDER_INSET;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterLocalVideoRecView;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;
import it.unimib.exercise.andrea.mediahandler.repository.IMediaRepository;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLocalVideoList extends Fragment{
    private RecyclerView recyclerViewLocalVideos;
    private List<LocalVideo> localVideos;
    AdapterLocalVideoRecView adapterLocalVideoRecView;
    private static final String TAG = FragmentLocalVideoList.class.getSimpleName();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final int STORAGE_PERMISSION = 101;
    private ViewModelMedia viewModelMedia;

    public FragmentLocalVideoList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IMediaRepository mediaRepository =
                ServiceLocator.getInstance().getMediaRepository(
                        requireActivity().getApplication()
                );
        if (mediaRepository != null){
            viewModelMedia = new ViewModelProvider(
                    requireActivity(),
                    new ViewModelMediaFactory(mediaRepository)).get(ViewModelMedia.class);
        }
        localVideos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewLocalVideos = view.findViewById(R.id.recyclerview_local_videos);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);
        adapterLocalVideoRecView = new AdapterLocalVideoRecView(localVideos, getActivity().getApplication(), new AdapterLocalVideoRecView.OnItemClickListener() {
            @Override
            public void onVideoClick(LocalVideo localVideo, int position) {
                FragmentLocalVideoListDirections.ActionFragmentVideoToFragmentLocalVideoPlayer action =
                        FragmentLocalVideoListDirections.actionFragmentVideoToFragmentLocalVideoPlayer(
                                localVideo);
                Navigation.findNavController(view).navigate(action);
            }
        });

        recyclerViewLocalVideos.setLayoutManager(layoutManager);
        recyclerViewLocalVideos.setAdapter(adapterLocalVideoRecView);
        //divider between items of recycle view
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        divider.setDividerInsetEnd(DIVIDER_INSET);
        divider.setLastItemDecorated(false);
        recyclerViewLocalVideos.addItemDecoration(divider);

        viewModelMedia.getLocalsVideo().observe(getViewLifecycleOwner(), resultLocalVideos ->{
            if (resultLocalVideos.isSuccess()){
                Log.d(TAG, "onViewCreated: isSuccess");
                localVideos.clear();
                List<LocalVideo> resultList = ((ResultLocalVideos.Success)resultLocalVideos).getData();
                localVideos.addAll(resultList);
                adapterLocalVideoRecView.notifyDataSetChanged();
            }
        });
    }
}