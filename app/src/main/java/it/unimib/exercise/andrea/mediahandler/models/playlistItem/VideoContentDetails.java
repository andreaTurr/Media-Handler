package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class VideoContentDetails implements Parcelable {
    private String videoId;

    public VideoContentDetails(String videoId) {this.videoId = videoId;}

    public String getVideoId() {return videoId;}
    public void setVideoId(String videoId) {this.videoId = videoId;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoId);
    }

    public void readFromParcel(Parcel source) {
        this.videoId = source.readString();
    }

    protected VideoContentDetails(Parcel in) {
        this.videoId = in.readString();
    }

    public static final Parcelable.Creator<VideoContentDetails> CREATOR = new Parcelable.Creator<VideoContentDetails>() {
        @Override
        public VideoContentDetails createFromParcel(Parcel source) {
            return new VideoContentDetails(source);
        }

        @Override
        public VideoContentDetails[] newArray(int size) {
            return new VideoContentDetails[size];
        }
    };
}
