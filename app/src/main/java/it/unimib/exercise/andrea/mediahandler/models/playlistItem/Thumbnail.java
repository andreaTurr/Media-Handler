package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Thumbnail implements Parcelable {
    @SerializedName("high")
    @Embedded
    private ImageHigh imageHighRes;
    @SerializedName("default")
    @Embedded
    private ImageDefault imageDefRes;

    public Thumbnail(ImageHigh imageHighRes, ImageDefault imageDefRes) {
        this.imageHighRes = imageHighRes;
        this.imageDefRes = imageDefRes;
    }
    @Ignore
    public Thumbnail() {}

    public ImageHigh getImageHighRes() {
        return imageHighRes;
    }

    public void setImageHighRes(ImageHigh imageHighRes) {
        this.imageHighRes = imageHighRes;
    }

    public ImageDefault getImageDefRes() {
        return imageDefRes;
    }

    public void setImageDefRes(ImageDefault imageDefRes) {
        this.imageDefRes = imageDefRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thumbnail thumbnail = (Thumbnail) o;
        return Objects.equals(imageHighRes, thumbnail.imageHighRes) && Objects.equals(imageDefRes, thumbnail.imageDefRes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageHighRes, imageDefRes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.imageHighRes, flags);
        dest.writeParcelable(this.imageDefRes, flags);
    }

    public void readFromParcel(Parcel source) {
        this.imageHighRes = source.readParcelable(ImageHigh.class.getClassLoader());
        this.imageDefRes = source.readParcelable(ImageDefault.class.getClassLoader());
    }


    protected Thumbnail(Parcel in) {
        this.imageHighRes = in.readParcelable(ImageHigh.class.getClassLoader());
        this.imageDefRes = in.readParcelable(ImageDefault.class.getClassLoader());
    }

    public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        @Override
        public Thumbnail createFromParcel(Parcel source) {
            return new Thumbnail(source);
        }

        @Override
        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };
}
