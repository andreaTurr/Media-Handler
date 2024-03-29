package it.unimib.exercise.andrea.mediahandler.models.localVideo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class LocalVideo implements Parcelable {

    @Ignore
    private Uri uri;
    @PrimaryKey
    @NonNull
    private String name;
    @ColumnInfo(name = "current_time", defaultValue = "0")
    private long currentTime;
    private int duration;
    private int size;
    @Ignore
    private Bitmap thumbNail;

    @Ignore
    public LocalVideo(Uri uri, @NonNull String name, long currentTime, int duration, int size, Bitmap thumbNail) {
        this.uri = uri;
        this.name = name;
        this.currentTime = currentTime;
        this.duration = duration;
        this.size = size;
        this.thumbNail = thumbNail;
    }

    public LocalVideo(@NonNull String name, long currentTime, int duration, int size) {
        this.thumbNail = thumbNail;
        this.uri = uri;
        this.name = name;
        this.currentTime = currentTime;
        this.duration = duration;
        this.size = size;
    }

    public Bitmap getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Bitmap thumbNail) {
        this.thumbNail = thumbNail;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "LocalVideo{" +
                "name='" + name + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.thumbNail, flags);
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.name);
        dest.writeInt(this.duration);
        dest.writeInt(this.size);
    }

    public void readFromParcel(Parcel source) {
        this.thumbNail = source.readParcelable(Bitmap.class.getClassLoader());
        this.uri = source.readParcelable(Uri.class.getClassLoader());
        this.name = source.readString();
        this.duration = source.readInt();
        this.size = source.readInt();
    }

    protected LocalVideo(Parcel in) {
        this.thumbNail = in.readParcelable(Bitmap.class.getClassLoader());
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.duration = in.readInt();
        this.size = in.readInt();
    }

    public static final Parcelable.Creator<LocalVideo> CREATOR = new Parcelable.Creator<LocalVideo>() {
        @Override
        public LocalVideo createFromParcel(Parcel source) {
            return new LocalVideo(source);
        }

        @Override
        public LocalVideo[] newArray(int size) {
            return new LocalVideo[size];
        }
    };
}
