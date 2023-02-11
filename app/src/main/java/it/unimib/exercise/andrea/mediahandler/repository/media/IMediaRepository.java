package it.unimib.exercise.andrea.mediahandler.repository.media;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;

public interface IMediaRepository {
    MutableLiveData<Result> fetchLocalVideo();
    MutableLiveData<Result> fetchInsertLocalVideo(LocalVideo localVideo);
    void updateLocalVideo(LocalVideo localVideo);

    MutableLiveData<Result> fetchLocalAudio();
    MutableLiveData<Result> fetchInsertLocalAudio(LocalAudio localAudio);
    void updateLocalAudio(LocalAudio localAudio);
}
