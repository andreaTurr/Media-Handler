package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import java.util.Objects;

public class VideoSnippet implements Parcelable {
    private String title;
    @Embedded
    private Thumbnail thumbnails;
    private String playlistId;
    private int position;

    public VideoSnippet(String title, Thumbnail thumbnails, String playlistId, int position) {
        this.title = title;
        this.thumbnails = thumbnails;
        this.playlistId = playlistId;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "VideoSnippet{" +
                "title='" + title + '\'' +
                ", thumbnails=" + thumbnails +
                ", playlistId='" + playlistId + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoSnippet that = (VideoSnippet) o;
        return position == that.position && Objects.equals(title, that.title) && Objects.equals(thumbnails, that.thumbnails) && Objects.equals(playlistId, that.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, thumbnails, playlistId, position);
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
        dest.writeInt(this.position);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.thumbnails = source.readParcelable(Thumbnail.class.getClassLoader());
        this.playlistId = source.readString();
        this.position = source.readInt();
    }

    public VideoSnippet() {
    }

    protected VideoSnippet(Parcel in) {
        this.title = in.readString();
        this.thumbnails = in.readParcelable(Thumbnail.class.getClassLoader());
        this.playlistId = in.readString();
        this.position = in.readInt();
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
