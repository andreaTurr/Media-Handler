package it.unimib.exercise.andrea.mediahandler.adapters;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

public class AdapterPlaylistRecView extends RecyclerView.Adapter<AdapterPlaylistRecView.ViewHolder> {

    private final List<Video> videoList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onVideoClick(Video video);
    }

    public AdapterPlaylistRecView(List<Video> videoList, Application application, OnItemClickListener onItemClickListener) {
        this.videoList = videoList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        if(videoList != null){
            return videoList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView video_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.txtview_playlist_name);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onVideoClick(videoList.get(getAdapterPosition()));
        }

        public void bind(Video video) {
            video_name.setText(video.getSnippet().getTitle());
        }
    }
}
