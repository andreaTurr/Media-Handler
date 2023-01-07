package it.unimib.exercise.andrea.mediahandler.source.media;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;

public abstract class BaseMediaDataSource {
    protected MediaCallback mediaCallback;

    public void setPlaylistCallback(MediaCallback mediaCallback) {
        this.mediaCallback = mediaCallback;
    }
    public abstract void getVideos();
    public abstract void getLocalVideo(LocalVideo localVideo);
    public abstract void updateLocalVideo(LocalVideo localVideo);

    public abstract void getAudios();
    public abstract void getLocalAudio(LocalAudio localAudio);
    public abstract void updateLocalAudio(LocalAudio localAudio);
}
