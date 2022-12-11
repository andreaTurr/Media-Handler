package it.unimib.exercise.andrea.mediahandler.models.playlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class PlaylistSnippet implements Parcelable {
    private String publishedAt;
    private String title;
    private String description;
    @Embedded
    private PlaylistThumbnail thumbnails;
    private String channelTitle;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlaylistThumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(PlaylistThumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publishedAt);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.thumbnails, flags);
        dest.writeString(this.channelTitle);
    }

    public void readFromParcel(Parcel source) {
        this.publishedAt = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        this.thumbnails = source.readParcelable(PlaylistThumbnail.class.getClassLoader());
        this.channelTitle = source.readString();
    }

    public PlaylistSnippet() {
    }

    protected PlaylistSnippet(Parcel in) {
        this.publishedAt = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.thumbnails = in.readParcelable(PlaylistThumbnail.class.getClassLoader());
        this.channelTitle = in.readString();
    }

    public static final Parcelable.Creator<PlaylistSnippet> CREATOR = new Parcelable.Creator<PlaylistSnippet>() {
        @Override
        public PlaylistSnippet createFromParcel(Parcel source) {
            return new PlaylistSnippet(source);
        }

        @Override
        public PlaylistSnippet[] newArray(int size) {
            return new PlaylistSnippet[size];
        }
    };
}
