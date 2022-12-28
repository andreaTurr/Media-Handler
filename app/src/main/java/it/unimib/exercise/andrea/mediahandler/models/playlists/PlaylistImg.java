package it.unimib.exercise.andrea.mediahandler.models.playlists;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class PlaylistImg implements Parcelable {
    @SerializedName("url")
    @ColumnInfo(name = "defImgUrl")
    private String defImgUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.defImgUrl);
    }

    public String getDefImgUrl() {
        return defImgUrl;
    }

    public void setDefImgUrl(String defImgUrl) {
        this.defImgUrl = defImgUrl;
    }


    public void readFromParcel(Parcel source) {
        this.defImgUrl = source.readString();
    }

    public PlaylistImg() {
    }

    protected PlaylistImg(Parcel in) {
        this.defImgUrl = in.readString();
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
