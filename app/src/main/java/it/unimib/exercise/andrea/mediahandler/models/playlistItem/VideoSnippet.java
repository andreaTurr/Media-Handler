package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoSnippet implements Parcelable {
    private String title;
    private List<VideoThumbnail> thumbnails;

    public VideoSnippet(String title, List<VideoThumbnail> thumbnails) {
        this.title = title;
        this.thumbnails = thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VideoThumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<VideoThumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoSnippet that = (VideoSnippet) o;
        return Objects.equals(title, that.title) && Objects.equals(thumbnails, that.thumbnails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, thumbnails);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeList(this.thumbnails);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.thumbnails = new ArrayList<VideoThumbnail>();
        source.readList(this.thumbnails, VideoThumbnail.class.getClassLoader());
    }

    protected VideoSnippet(Parcel in) {
        this.title = in.readString();
        this.thumbnails = new ArrayList<VideoThumbnail>();
        in.readList(this.thumbnails, VideoThumbnail.class.getClassLoader());
    }

    public static final Parcelable.Creator<VideoSnippet> CREATOR = new Parcelable.Creator<VideoSnippet>() {
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
