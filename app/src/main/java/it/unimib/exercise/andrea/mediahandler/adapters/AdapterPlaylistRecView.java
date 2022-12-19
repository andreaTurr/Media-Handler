package it.unimib.exercise.andrea.mediahandler.adapters;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;

public class AdapterPlaylistRecView extends
        RecyclerView.Adapter<AdapterPlaylistRecView.ViewHolder> {
    List<Playlist> playlistList;
    Application application;
    OnItemClickListener onItemClickListener;

    public AdapterPlaylistRecView(List<Playlist> playlistList, Application application, OnItemClickListener onItemClickListener) {
        this.playlistList = playlistList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onPlaylistClick(Playlist playlist);
    }

    @NonNull
    @Override
    public AdapterPlaylistRecView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlaylistRecView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView playlist_name;
        private final ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlist_name = itemView.findViewById(R.id.txtview_playlist_name);
            imageViewThumbnail = itemView.findViewById(R.id.imageView_thumbnail);

            itemView.setOnClickListener(this);
            // Define click listener for the ViewHolder's View
        }

        @Override
        public void onClick(View view) {

        }
    }
}
