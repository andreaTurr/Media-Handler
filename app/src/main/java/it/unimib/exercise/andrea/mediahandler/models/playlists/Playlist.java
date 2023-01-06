package it.unimib.exercise.andrea.mediahandler.models.playlists;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Class to represent the playlist of youtube API v3
 */
@Entity
public class Playlist implements Parcelable {

    // Used for Room
    @PrimaryKey
    @NonNull
    private String id;
    @Embedded
    private PlaylistSnippet snippet;
    @Embedded
    private PlaylistContentDetails contentDetails;
    // @ColumnInfo(name = "user_name", defaultValue = "temp") val name: String
    @ColumnInfo(name = "last_update", defaultValue = "0")
    private long lastUpdate;
    @ColumnInfo(name = "total_duration", defaultValue = "0")
    private long totalDuration;

    //contrsuctor

    public Playlist(@NonNull String id, PlaylistSnippet snippet, PlaylistContentDetails contentDetails, long lastUpdate, long totalDuration) {
        this.id = id;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
        this.lastUpdate = lastUpdate;
        this.totalDuration = totalDuration;
    }


    //getter and setters

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    //tostring
    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", snippet=" + snippet +
                ", contentDetails=" + contentDetails +
                ", lastUpdate=" + lastUpdate +
                ", totalDuration=" + totalDuration +
                '}';
    }

    //equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return id.equals(playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    //parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.snippet, flags);
        dest.writeParcelable(this.contentDetails, flags);
        dest.writeLong(this.lastUpdate);
        dest.writeLong(this.totalDuration);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.snippet = source.readParcelable(PlaylistSnippet.class.getClassLoader());
        this.contentDetails = source.readParcelable(PlaylistContentDetails.class.getClassLoader());
        this.lastUpdate = source.readLong();
        this.totalDuration = source.readLong();
    }

    protected Playlist(Parcel in) {
        this.id = in.readString();
        this.snippet = in.readParcelable(PlaylistSnippet.class.getClassLoader());
        this.contentDetails = in.readParcelable(PlaylistContentDetails.class.getClassLoader());
        this.lastUpdate = in.readLong();
        this.totalDuration = in.readLong();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
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
