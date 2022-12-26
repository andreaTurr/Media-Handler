package it.unimib.exercise.andrea.mediahandler.models.playlists;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class to represent the playlist of youtube playlist API
 */

public class PlaylistApiResponse implements Parcelable {
    private PlaylistPageInfo pageInfo;

    @SerializedName("items")
    private List<Playlist> playlistList;

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.pageInfo, flags);
        dest.writeTypedList(this.playlistList);
    }

    public void readFromParcel(Parcel source) {
        this.pageInfo = source.readParcelable(PlaylistPageInfo.class.getClassLoader());
        this.playlistList = source.createTypedArrayList(Playlist.CREATOR);
    }

    public PlaylistApiResponse(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    protected PlaylistApiResponse(Parcel in) {
        this.pageInfo = in.readParcelable(PlaylistPageInfo.class.getClassLoader());
        this.playlistList = in.createTypedArrayList(Playlist.CREATOR);
    }

    public static final Parcelable.Creator<PlaylistApiResponse> CREATOR = new Parcelable.Creator<PlaylistApiResponse>() {
        @Override
        public PlaylistApiResponse createFromParcel(Parcel source) {
            return new PlaylistApiResponse(source);
        }

        @Override
        public PlaylistApiResponse[] newArray(int size) {
            return new PlaylistApiResponse[size];
        }
    };
}
