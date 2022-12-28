package it.unimib.exercise.andrea.mediahandler.models.playlists;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

public class PlaylistSnippet implements Parcelable {
    private String title;
    private String description;
    @Embedded
    private PlaylistThumbnail thumbnails;

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



    @Override
    public String toString() {
        return "PlaylistSnippet{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnails=" + thumbnails +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.thumbnails, flags);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.thumbnails = source.readParcelable(PlaylistThumbnail.class.getClassLoader());
    }

    public PlaylistSnippet() {
    }

    protected PlaylistSnippet(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.thumbnails = in.readParcelable(PlaylistThumbnail.class.getClassLoader());
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
