package it.unimib.exercise.andrea.mediahandler.models.localAudio;

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
public class LocalAudio implements Parcelable {
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
    private String author;


    public LocalAudio(@NonNull String name, long currentTime, int duration, int size) {
        this.name = name;
        this.currentTime = currentTime;
        this.duration = duration;
        this.size = size;
    }


    @Ignore
    public LocalAudio(Uri uri, @NonNull String name, long currentTime, int duration, int size, Bitmap thumbNail, String author) {
        this.uri = uri;
        this.name = name;
        this.currentTime = currentTime;
        this.duration = duration;
        this.size = size;
        this.thumbNail = thumbNail;
        this.author = author;
    }
    @Ignore
    public LocalAudio(Uri uri, @NonNull String name, long currentTime, int duration, int size, String author) {
        this.uri = uri;
        this.name = name;
        this.currentTime = currentTime;
        this.duration = duration;
        this.size = size;
        this.author = author;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
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

    public Bitmap getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Bitmap thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "LocalAudio{" +
                "name='" + name + '\'' +
                ", currentTime=" + currentTime +
                ", duration=" + duration +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.name);
        dest.writeLong(this.currentTime);
        dest.writeInt(this.duration);
        dest.writeInt(this.size);
        dest.writeParcelable(this.thumbNail, flags);
        dest.writeString(this.author);
    }

    public void readFromParcel(Parcel source) {
        this.uri = source.readParcelable(Uri.class.getClassLoader());
        this.name = source.readString();
        this.currentTime = source.readLong();
        this.duration = source.readInt();
        this.size = source.readInt();
        this.thumbNail = source.readParcelable(Bitmap.class.getClassLoader());
        this.author = source.readString();
    }

    protected LocalAudio(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.currentTime = in.readLong();
        this.duration = in.readInt();
        this.size = in.readInt();
        this.thumbNail = in.readParcelable(Bitmap.class.getClassLoader());
        this.author = in.readString();
    }

    public static final Creator<LocalAudio> CREATOR = new Creator<LocalAudio>() {
        @Override
        public LocalAudio createFromParcel(Parcel source) {
            return new LocalAudio(source);
        }

        @Override
        public LocalAudio[] newArray(int size) {
            return new LocalAudio[size];
        }
    };
}
