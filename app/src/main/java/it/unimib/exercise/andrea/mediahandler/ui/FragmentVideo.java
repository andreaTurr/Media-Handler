package it.unimib.exercise.andrea.mediahandler.ui;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.DIVIDER_INSET;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterLocalVideoRecView;
import it.unimib.exercise.andrea.mediahandler.adapters.AdapterPlaylistRecView;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentVideo extends Fragment implements AdapterLocalVideoRecView.OnItemClickListener{
    private RecyclerView recyclerViewLocalVideos;
    private List<LocalVideo> localVideos;
    AdapterLocalVideoRecView adapterLocalVideoRecView;

    public FragmentVideo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        adapterLocalVideoRecView = new AdapterLocalVideoRecView(localVideos, getActivity().getApplication(), this::onVideoClick);

        recyclerViewLocalVideos.setLayoutManager(layoutManager);
        recyclerViewLocalVideos.setAdapter(adapterLocalVideoRecView);
        //divider between items of recycle view
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        divider.setDividerInsetEnd(DIVIDER_INSET);
        divider.setLastItemDecorated(false);
        recyclerViewLocalVideos.addItemDecoration(divider);

        getVideos();

    }

    private void getVideos() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String videoTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap videoThumbNail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                localVideos.add(new LocalVideo(videoTitle, videoPath, videoThumbNail));
            }while(cursor.moveToNext());
        }
        adapterLocalVideoRecView.notifyDataSetChanged();

    }

    @Override
    public void onVideoClick(LocalVideo video, int position) {

    }
}