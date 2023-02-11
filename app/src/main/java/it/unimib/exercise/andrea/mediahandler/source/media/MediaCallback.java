package it.unimib.exercise.andrea.mediahandler.source.media;

import it.unimib.exercise.andrea.mediahandler.models.Result;

public interface MediaCallback {
    void onSuccessFromStorageVideo(Result localVideos);
    void onSuccessFromGetVideoCurrentTime(Result localVideo);

    void onSuccessFromStorageAudio(Result localAudios);
    void onSuccessFromGetAudioCurrentTime(Result localAudio);
}
