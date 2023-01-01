package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import java.util.Objects;

public class ImageHigh implements Parcelable {
    @ColumnInfo(name = "ImgUrlHigh")
    String url;

    public ImageHigh(String url) {
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
        ImageHigh imageHigh = (ImageHigh) o;
        return Objects.equals(url, imageHigh.url);
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

    protected ImageHigh(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ImageHigh> CREATOR = new Parcelable.Creator<ImageHigh>() {
        @Override
        public ImageHigh createFromParcel(Parcel source) {
            return new ImageHigh(source);
        }

        @Override
        public ImageHigh[] newArray(int size) {
            return new ImageHigh[size];
        }
    };
}
