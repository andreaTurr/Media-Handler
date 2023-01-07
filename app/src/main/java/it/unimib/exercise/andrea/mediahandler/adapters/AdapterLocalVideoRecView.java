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
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;

public class AdapterLocalVideoRecView extends RecyclerView.Adapter<AdapterLocalVideoRecView.ViewHolder> {
    private static final String TAG = AdapterPlaylistRecView.class.getSimpleName();
    private final List<LocalVideo> videoList;
    private final Application application;
    private final AdapterLocalVideoRecView.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onVideoClick(LocalVideo video, int position);
    }

    public AdapterLocalVideoRecView(List<LocalVideo> videoList, Application application, AdapterLocalVideoRecView.OnItemClickListener onItemClickListener) {
        this.videoList = videoList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_playlist_item, parent, false);
        return new AdapterLocalVideoRecView.ViewHolder(view);
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
        private final ImageView videoThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.video_name = itemView.findViewById(R.id.txtview_playlist_name);
            this.videoThumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        public void bind(LocalVideo video) {
            String url;
            String title;
            video_name.setText("");


        }
    }
}
