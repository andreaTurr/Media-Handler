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

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterLocalAudioRecView;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.repository.IMediaRepository;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLocalAudioList extends Fragment {
    private static final String TAG = FragmentLocalAudioList.class.getSimpleName();
    private ViewModelMedia viewModelMedia;
    private List<LocalAudio> localAudios;
    private RecyclerView recyclerViewLocalAudios;
    AdapterLocalAudioRecView adapterLocalAudioRecView;

    public FragmentLocalAudioList() {
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
        localAudios = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewLocalAudios = view.findViewById(R.id.recyclerview_local_audios);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);
        adapterLocalAudioRecView = new AdapterLocalAudioRecView(localAudios, getActivity().getApplication(), new AdapterLocalAudioRecView.OnItemClickListener() {
            @Override
            public void onVideoClick(LocalAudio LocalAudio, int position) {
                FragmentLocalAudioListDirections.ActionFragmentAudioToFragmentAudioPlayer action =
                        FragmentLocalAudioListDirections.actionFragmentAudioToFragmentAudioPlayer(
                                LocalAudio);
                Navigation.findNavController(view).navigate(action);
            }
        });

        recyclerViewLocalAudios.setLayoutManager(layoutManager);
        recyclerViewLocalAudios.setAdapter(adapterLocalAudioRecView);
        //divider between items of recycle view
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        divider.setDividerInsetEnd(DIVIDER_INSET);
        divider.setLastItemDecorated(false);
        recyclerViewLocalAudios.addItemDecoration(divider);

        viewModelMedia.getLocalAudio().observe(getViewLifecycleOwner(), resultLocalAudios ->{
            if (resultLocalAudios.isSuccess()){
                Log.d(TAG, "onViewCreated: local isSuccess");
                localAudios.clear();
                List<LocalAudio> resultList = ((ResultLocalAudios.Success)resultLocalAudios).getData();
                localAudios.addAll(resultList);
                adapterLocalAudioRecView.notifyDataSetChanged();
            }
        });
    }
}