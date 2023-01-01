package it.unimib.exercise.andrea.mediahandler.adapters;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;

public class AdapterPlaylistRecView extends RecyclerView.Adapter<AdapterPlaylistRecView.ViewHolder> {

    private static final String TAG = AdapterPlaylistRecView.class.getSimpleName();
    private final List<Video> videoList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onVideoClick(Video video, int position);
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
        private final ImageView videoThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.video_name = itemView.findViewById(R.id.txtview_playlist_name);
            this.videoThumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onVideoClick(videoList.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition());
        }

        public void bind(Video video) {
            String url;
            String title = video.getSnippet().getTitle();
            video_name.setText(title);
            if(video.getSnippet().getThumbnails() == null){
                url = null;
            }else if (video.getSnippet().getThumbnails().getImageHighRes() != null){
                url = video.getSnippet().getThumbnails().getImageHighRes().getUrl();
            }else if (video.getSnippet().getThumbnails().getImageDefRes() != null){
                url = video.getSnippet().getThumbnails().getImageDefRes().getUrl();
            }else{
                Log.d(TAG, "bind: have thumbnail but no image");
                url = null;
            }

            //Log.d(TAG, "bind: " + url);
            if (url != null) {
                Glide.with(application)
                        .load(url)
                        .placeholder(R.drawable.ic_baseline_cloud_download_24)
                        .into(videoThumbnail);
            }else{
                //Log.d(TAG, "null: ");
                if (title.equalsIgnoreCase("Deleted video")){
                    videoThumbnail.setImageResource(R.drawable.ic_baseline_delete_24);
                }else if(title.equalsIgnoreCase("Private video")){
                    videoThumbnail.setImageResource(R.drawable.ic_baseline_privacy_tip_24);
                }
                videoThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

        }
    }
}
