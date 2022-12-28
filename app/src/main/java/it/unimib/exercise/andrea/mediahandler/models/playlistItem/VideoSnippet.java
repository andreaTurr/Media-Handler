package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoSnippet implements Parcelable {
    private String title;
    @Embedded
    private VideoThumbnail thumbnails;
    private String playlistId;

    public VideoSnippet(String title, VideoThumbnail thumbnails, String playlistId) {
        this.title = title;
        this.thumbnails = thumbnails;
        this.playlistId = playlistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VideoThumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(VideoThumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoSnippet that = (VideoSnippet) o;
        return Objects.equals(title, that.title) && Objects.equals(thumbnails, that.thumbnails) && Objects.equals(playlistId, that.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, thumbnails, playlistId);
    }

    @Override
    public String toString() {
        return "VideoSnippet{" +
                "title='" + title + '\'' +
                ", thumbnails=" + thumbnails +
                ", playlistId='" + playlistId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeParcelable(this.thumbnails, flags);
        dest.writeString(this.playlistId);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.thumbnails = source.readParcelable(VideoThumbnail.class.getClassLoader());
        this.playlistId = source.readString();
    }

    public VideoSnippet() {
    }

    protected VideoSnippet(Parcel in) {
        this.title = in.readString();
        this.thumbnails = in.readParcelable(VideoThumbnail.class.getClassLoader());
        this.playlistId = in.readString();
    }

    public static final Creator<VideoSnippet> CREATOR = new Creator<VideoSnippet>() {
        @Override
        public VideoSnippet createFromParcel(Parcel source) {
            return new VideoSnippet(source);
        }

        @Override
        public VideoSnippet[] newArray(int size) {
            return new VideoSnippet[size];
        }
    };
}
