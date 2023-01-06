package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity
public class VideoPartial {
    // The ID that YouTube uses to uniquely identify the playlist item.
    @ColumnInfo(name = "id_video_in_playlist")
    private String idVideoInPlaylist;
    @Embedded
    private VideoSnippet snippet;
    @Embedded
    private VideoContentDetails contentDetails;

    @Ignore
    public VideoPartial(Video video) {
        this.idVideoInPlaylist = video.getIdVideoInPlaylist();
        this.snippet = video.getSnippet();
        this.contentDetails = video.getContentDetails();
    }

    public VideoPartial(String idVideoInPlaylist, VideoSnippet snippet, VideoContentDetails contentDetails) {
        this.idVideoInPlaylist = idVideoInPlaylist;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
    }

    public String getIdVideoInPlaylist() {
        return idVideoInPlaylist;
    }

    public void setIdVideoInPlaylist(String idVideoInPlaylist) {
        this.idVideoInPlaylist = idVideoInPlaylist;
    }

    public VideoSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoSnippet snippet) {
        this.snippet = snippet;
    }

    public VideoContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(VideoContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }
}

