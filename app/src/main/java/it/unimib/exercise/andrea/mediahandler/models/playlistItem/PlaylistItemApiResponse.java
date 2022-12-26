package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistItemApiResponse {
    @SerializedName("items")
    private List<Video> videoList;
}
