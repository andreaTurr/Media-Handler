package it.unimib.exercise.andrea.mediahandler.models.video;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoApiResponse {
    @SerializedName("items")
    private List<VideoDetailed> videoDetailedList;
    private PageInfo pageInfo;

    public VideoApiResponse(List<VideoDetailed> videoDetailedList, PageInfo pageInfo) {
        this.videoDetailedList = videoDetailedList;
        this.pageInfo = pageInfo;
    }

    public List<VideoDetailed> getVideoDetailedList() {
        return videoDetailedList;
    }

    public void setVideoDetailedList(List<VideoDetailed> videoDetailedList) {
        this.videoDetailedList = videoDetailedList;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
