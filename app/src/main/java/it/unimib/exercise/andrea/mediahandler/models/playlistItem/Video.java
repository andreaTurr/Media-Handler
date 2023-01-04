package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity
public class Video implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_video_in_playlist")
    @SerializedName("id")
    // The ID that YouTube uses to uniquely identify the playlist item.
    private String idVideoInPlaylist;
    @Embedded
    private VideoSnippet snippet;
    @Embedded
    private VideoContentDetails contentDetails;

    @ColumnInfo(defaultValue = "0")
    private float videoDuration;
    @ColumnInfo(defaultValue = "0")
    private float currentSecond;

    public Video(@NonNull String idVideoInPlaylist, VideoSnippet snippet, VideoContentDetails contentDetails, float videoDuration, float currentSecond) {
        this.idVideoInPlaylist = idVideoInPlaylist;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
        this.videoDuration = videoDuration;
        this.currentSecond = currentSecond;
    }

    @NonNull
    public String getIdVideoInPlaylist() {
        return idVideoInPlaylist;
    }

    public void setIdVideoInPlaylist(@NonNull String idVideoInPlaylist) {
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

    public float getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(float videoDuration) {
        this.videoDuration = videoDuration;
    }

    public float getCurrentSecond() {
        return currentSecond;
    }

    public void setCurrentSecond(float currentSecond) {
        this.currentSecond = currentSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Float.compare(video.videoDuration, videoDuration) == 0 && Float.compare(video.currentSecond, currentSecond) == 0 && idVideoInPlaylist.equals(video.idVideoInPlaylist) && Objects.equals(snippet, video.snippet) && Objects.equals(contentDetails, video.contentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVideoInPlaylist, snippet, contentDetails, videoDuration, currentSecond);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + idVideoInPlaylist + '\'' +
                ", snippet=" + snippet +
                ", contentDetails=" + contentDetails +
                ", videoDuration=" + videoDuration +
                ", currentSecond=" + currentSecond +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idVideoInPlaylist);
        dest.writeParcelable(this.snippet, flags);
        dest.writeParcelable(this.contentDetails, flags);
        dest.writeFloat(this.videoDuration);
        dest.writeFloat(this.currentSecond);
    }

    public void readFromParcel(Parcel source) {
        this.idVideoInPlaylist = source.readString();
        this.snippet = source.readParcelable(VideoSnippet.class.getClassLoader());
        this.contentDetails = source.readParcelable(VideoContentDetails.class.getClassLoader());
        this.videoDuration = source.readFloat();
        this.currentSecond = source.readFloat();
    }
    @Ignore
    public Video() {
    }

    protected Video(Parcel in) {
        this.idVideoInPlaylist = in.readString();
        this.snippet = in.readParcelable(VideoSnippet.class.getClassLoader());
        this.contentDetails = in.readParcelable(VideoContentDetails.class.getClassLoader());
        this.videoDuration = in.readFloat();
        this.currentSecond = in.readFloat();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
