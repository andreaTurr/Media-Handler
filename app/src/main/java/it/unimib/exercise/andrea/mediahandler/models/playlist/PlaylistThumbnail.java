package it.unimib.exercise.andrea.mediahandler.models.playlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class PlaylistThumbnail implements Parcelable {
    @SerializedName("default")
    @Embedded
    private PlaylistImg image;

    public PlaylistImg getImage() {
        return image;
    }

    public void setImage(PlaylistImg image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.image, flags);
    }

    public void readFromParcel(Parcel source) {
        this.image = source.readParcelable(PlaylistImg.class.getClassLoader());
    }

    public PlaylistThumbnail() {
    }

    protected PlaylistThumbnail(Parcel in) {
        this.image = in.readParcelable(PlaylistImg.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlaylistThumbnail> CREATOR = new Parcelable.Creator<PlaylistThumbnail>() {
        @Override
        public PlaylistThumbnail createFromParcel(Parcel source) {
            return new PlaylistThumbnail(source);
        }

        @Override
        public PlaylistThumbnail[] newArray(int size) {
            return new PlaylistThumbnail[size];
        }
    };
}
