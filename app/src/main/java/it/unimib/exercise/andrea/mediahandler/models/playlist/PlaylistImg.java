package it.unimib.exercise.andrea.mediahandler.models.playlist;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistImg implements Parcelable {
    private String url;
    private int width;
    private int height;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.width = source.readInt();
        this.height = source.readInt();
    }

    public PlaylistImg() {
    }

    protected PlaylistImg(Parcel in) {
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
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
