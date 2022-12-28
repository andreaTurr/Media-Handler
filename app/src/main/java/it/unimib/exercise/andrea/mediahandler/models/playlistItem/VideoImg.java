package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class VideoImg implements Parcelable {
    String url;

    public VideoImg(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoImg videoImg = (VideoImg) o;
        return Objects.equals(url, videoImg.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
    }

    protected VideoImg(Parcel in) {
        this.url = in.readString();
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
