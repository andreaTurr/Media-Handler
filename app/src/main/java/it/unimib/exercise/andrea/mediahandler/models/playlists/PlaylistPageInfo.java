package it.unimib.exercise.andrea.mediahandler.models.playlists;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistPageInfo implements Parcelable {
    private int totalResults;
    private int resultsPerPage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalResults);
        dest.writeInt(this.resultsPerPage);
    }

    public void readFromParcel(Parcel source) {
        this.totalResults = source.readInt();
        this.resultsPerPage = source.readInt();
    }

    public PlaylistPageInfo() {
    }

    protected PlaylistPageInfo(Parcel in) {
        this.totalResults = in.readInt();
        this.resultsPerPage = in.readInt();
    }

    public static final Parcelable.Creator<PlaylistPageInfo> CREATOR = new Parcelable.Creator<PlaylistPageInfo>() {
        @Override
        public PlaylistPageInfo createFromParcel(Parcel source) {
            return new PlaylistPageInfo(source);
        }

        @Override
        public PlaylistPageInfo[] newArray(int size) {
            return new PlaylistPageInfo[size];
        }
    };
}
