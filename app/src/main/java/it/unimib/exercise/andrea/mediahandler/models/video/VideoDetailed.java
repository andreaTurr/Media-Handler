package it.unimib.exercise.andrea.mediahandler.models.video;

import com.google.gson.annotations.SerializedName;

public class VideoDetailed {
    @SerializedName("contentDetails")
    VideoDetailedContentDetails videoDetailedContentDetails;

    public VideoDetailed(VideoDetailedContentDetails videoDetailedContentDetails) {
        this.videoDetailedContentDetails = videoDetailedContentDetails;
    }

    public VideoDetailedContentDetails getVideoDetailedContentDetails() {
        return videoDetailedContentDetails;
    }

    public void setVideoDetailedContentDetails(VideoDetailedContentDetails videoDetailedContentDetails) {
        this.videoDetailedContentDetails = videoDetailedContentDetails;
    }
}
