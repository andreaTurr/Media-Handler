package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import java.util.Objects;

public class ImageDefault implements Parcelable{
    @ColumnInfo(name = "ImgUrlDefault")
    String url;

    public ImageDefault(String url) {
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
        ImageDefault that = (ImageDefault) o;
        return Objects.equals(url, that.url);
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

    public ImageDefault() {
    }

    protected ImageDefault(Parcel in) {
        this.url = in.readString();
    }

    public static final Creator<ImageDefault> CREATOR = new Creator<ImageDefault>() {
        @Override
        public ImageDefault createFromParcel(Parcel source) {
            return new ImageDefault(source);
        }

        @Override
        public ImageDefault[] newArray(int size) {
            return new ImageDefault[size];
        }
    };
}
