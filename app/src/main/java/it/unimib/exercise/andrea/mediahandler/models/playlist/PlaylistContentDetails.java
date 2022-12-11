package it.unimib.exercise.andrea.mediahandler.models.playlist;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaylistContentDetails implements Parcelable {
    private int itemCount;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemCount);
    }

    public void readFromParcel(Parcel source) {
        this.itemCount = source.readInt();
    }

    public PlaylistContentDetails() {
    }

    protected PlaylistContentDetails(Parcel in) {
        this.itemCount = in.readInt();
    }

    public static final Parcelable.Creator<PlaylistContentDetails> CREATOR = new Parcelable.Creator<PlaylistContentDetails>() {
        @Override
        public PlaylistContentDetails createFromParcel(Parcel source) {
            return new PlaylistContentDetails(source);
        }

        @Override
        public PlaylistContentDetails[] newArray(int size) {
            return new PlaylistContentDetails[size];
        }
    };
}
