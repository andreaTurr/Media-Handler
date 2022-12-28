package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video implements Parcelable {
    @PrimaryKey
    @NonNull
    // The ID that YouTube uses to uniquely identify the playlist item.
    private String id;
    @Embedded
    private VideoSnippet snippet;
    @Embedded
    private VideoContentDetails contentDetails;

    public Video(@NonNull String id, VideoSnippet snippet, VideoContentDetails contentDetails) {
        this.id = id;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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



    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", snippet=" + snippet +
                ", ContentDetails=" + contentDetails +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable((Parcelable) this.snippet, flags);
        dest.writeParcelable(this.contentDetails, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.snippet = source.readParcelable(VideoSnippet.class.getClassLoader());
        this.contentDetails = source.readParcelable(VideoContentDetails.class.getClassLoader());
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.snippet = in.readParcelable(VideoSnippet.class.getClassLoader());
        this.contentDetails = in.readParcelable(VideoContentDetails.class.getClassLoader());
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
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
