package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class VideoThumbnail implements Parcelable {
    @SerializedName("default")
    @Embedded
    private VideoImg image;

    public VideoThumbnail(VideoImg image) {
        this.image = image;
    }

    public VideoImg getImage() {
        return image;
    }

    public void setImage(VideoImg image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoThumbnail that = (VideoThumbnail) o;
        return Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image);
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
        this.image = source.readParcelable(VideoImg.class.getClassLoader());
    }

    protected VideoThumbnail(Parcel in) {
        this.image = in.readParcelable(VideoImg.class.getClassLoader());
    }

    public static final Parcelable.Creator<VideoThumbnail> CREATOR = new Parcelable.Creator<VideoThumbnail>() {
        @Override
        public VideoThumbnail createFromParcel(Parcel source) {
            return new VideoThumbnail(source);
        }

        @Override
        public VideoThumbnail[] newArray(int size) {
            return new VideoThumbnail[size];
        }
    };
}
