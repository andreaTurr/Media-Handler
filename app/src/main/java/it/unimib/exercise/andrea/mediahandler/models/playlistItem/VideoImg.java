package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class VideoImg implements Parcelable {
    String url;
    int width;
    int height;

    public VideoImg(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoImg videoImg = (VideoImg) o;
        return width == videoImg.width && height == videoImg.height && Objects.equals(url, videoImg.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, width, height);
    }

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

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.width = source.readInt();
        this.height = source.readInt();
    }

    protected VideoImg(Parcel in) {
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<VideoImg> CREATOR = new Parcelable.Creator<VideoImg>() {
        @Override
        public VideoImg createFromParcel(Parcel source) {
            return new VideoImg(source);
        }

        @Override
        public VideoImg[] newArray(int size) {
            return new VideoImg[size];
        }
    };
}
