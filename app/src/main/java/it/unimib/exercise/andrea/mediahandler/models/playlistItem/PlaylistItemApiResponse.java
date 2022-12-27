package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class PlaylistItemApiResponse implements Parcelable {
    @SerializedName("items")
    private List<Video> videoList;

    public PlaylistItemApiResponse(List<Video> videoList) {
        this.videoList = videoList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistItemApiResponse that = (PlaylistItemApiResponse) o;
        return Objects.equals(videoList, that.videoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.videoList);
    }

    public void readFromParcel(Parcel source) {
        this.videoList = source.createTypedArrayList(Video.CREATOR);
    }

    protected PlaylistItemApiResponse(Parcel in) {
        this.videoList = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Parcelable.Creator<PlaylistItemApiResponse> CREATOR = new Parcelable.Creator<PlaylistItemApiResponse>() {
        @Override
        public PlaylistItemApiResponse createFromParcel(Parcel source) {
            return new PlaylistItemApiResponse(source);
        }

        @Override
        public PlaylistItemApiResponse[] newArray(int size) {
            return new PlaylistItemApiResponse[size];
        }
    };
}
