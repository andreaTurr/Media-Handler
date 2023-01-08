package it.unimib.exercise.andrea.mediahandler.adapters;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;

public class AdapterLocalAudioRecView extends RecyclerView.Adapter<AdapterLocalAudioRecView.ViewHolder> {
    private static final String TAG = AdapterPlaylistRecView.class.getSimpleName();
    private final List<LocalAudio> localAudios;
    private final Application application;
    private final AdapterLocalAudioRecView.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onVideoClick(LocalAudio LocalAudio, int position);
    }

    public AdapterLocalAudioRecView(List<LocalAudio> localAudios, Application application, AdapterLocalAudioRecView.OnItemClickListener onItemClickListener) {
        this.localAudios = localAudios;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_playlist_item_audio, parent, false);
        return new AdapterLocalAudioRecView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(localAudios.get(position));
    }

    @Override
    public int getItemCount() {
        if(localAudios != null){
            return localAudios.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView audioName;
        private final TextView audioBody;
        private final ImageView thumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.audioName = itemView.findViewById(R.id.txtview_playlist_name_audio);
            this.audioBody = itemView.findViewById(R.id.textView_playlist_audio_body);
            this.thumbnail = itemView.findViewById(R.id.imageView_thumbnail_audio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onVideoClick(localAudios.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition() );
        }

        public void bind(LocalAudio audio) {
            String url;
            String title;
            audioName.setText(audio.getName());
            audioBody.setText(audio.getAuthor());
            if (audio.getThumbNail() == null){
                thumbnail.setImageResource(R.drawable.music_vector);
            }else{
                thumbnail.setImageBitmap(audio.getThumbNail());
            }

        }
    }
}
