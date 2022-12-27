package it.unimib.exercise.andrea.mediahandler.adapters;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

public class AdapterPlaylistsListRecView extends
        RecyclerView.Adapter<AdapterPlaylistsListRecView.ViewHolder> {
    private final static String TAG = AdapterPlaylistsListRecView.class.getSimpleName();
    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onPlaylistClick(Playlist playlist);
    }

    private final List<Playlist> playlistList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public AdapterPlaylistsListRecView(List<Playlist> playlistList, Application application, OnItemClickListener onItemClickListener) {
        this.playlistList = playlistList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlaylistsListRecView.ViewHolder holder, int position) {
        holder.bind(playlistList.get(position));
    }

    @Override
    public int getItemCount() {
        if (playlistList != null){
            return playlistList.size();
        }
        return 0;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            //Snackbar.make(view, playlist_name.getText(), Snackbar.LENGTH_SHORT).show();
            onItemClickListener.onPlaylistClick(playlistList.get(getAdapterPosition()));
        }

        public void bind(Playlist playlist) {
            playlist_name.setText(playlist.getSnippet().getTitle().toString());
            //Log.d(TAG, "bind: " + playlist.getSnippet().getTitle().toString());
            //imageViewThumbnail = ;
        }
    }
}
