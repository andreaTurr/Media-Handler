package it.unimib.exercise.andrea.mediahandler.source.media;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;

public interface MediaCallback {
    void onSuccessFromStorageVideo(ResultLocalVideos localVideos);
    void onSuccessFromGetVideoCurrentTime(ResultLocalVideos localVideo);

    void onSuccessFromStorageAudio(ResultLocalAudios localAudios);
    void onSuccessFromGetAudioCurrentTime(ResultLocalAudios localAudio);
}
