package it.unimib.exercise.andrea.mediahandler.models.playlist;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Class to represent the playlist of youtube API v3
 */
@Entity
public class Playlist implements Parcelable {

    // Used for Room
    @PrimaryKey(autoGenerate = true)
    private long id;
    @Embedded
    private PlaylistSnippet snippet;
    @Embedded
    private PlaylistContentDetails contentDetails;

    //getter and setters

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public PlaylistSnippet getSnippet() {
        return snippet;
    }
    public void setSnippet(PlaylistSnippet snippet) {
        this.snippet = snippet;
    }
    public PlaylistContentDetails getContentDetails() {
        return contentDetails;
    }
    public void setContentDetails(PlaylistContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.snippet, flags);
        dest.writeParcelable(this.contentDetails, flags);
    }

    public void readFromParcel(Parcel source) {
        this.snippet = source.readParcelable(PlaylistSnippet.class.getClassLoader());
        this.contentDetails = source.readParcelable(PlaylistContentDetails.class.getClassLoader());
    }

    public Playlist() {
    }

    protected Playlist(Parcel in) {
        this.snippet = in.readParcelable(PlaylistSnippet.class.getClassLoader());
        this.contentDetails = in.readParcelable(PlaylistContentDetails.class.getClassLoader());
    }

    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
