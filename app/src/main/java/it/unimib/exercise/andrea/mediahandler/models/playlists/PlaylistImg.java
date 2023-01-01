package it.unimib.exercise.andrea.mediahandler.models.playlists;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

public class PlaylistImg implements Parcelable {
    @ColumnInfo(name = "ImgUrl")
    private String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
    }

    public PlaylistImg() {
    }

    protected PlaylistImg(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<PlaylistImg> CREATOR = new Parcelable.Creator<PlaylistImg>() {
        @Override
        public PlaylistImg createFromParcel(Parcel source) {
            return new PlaylistImg(source);
        }

        @Override
        public PlaylistImg[] newArray(int size) {
            return new PlaylistImg[size];
        }
    };
}
