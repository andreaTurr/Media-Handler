package it.unimib.exercise.andrea.mediahandler.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;

public interface IMediaRepository {
    MutableLiveData<ResultLocalVideos> fetchLocalVideo();
    MutableLiveData<ResultLocalVideos> fetchLocalVideo(LocalVideo localVideo);
    void updateLocalVideo(LocalVideo localVideo);

    MutableLiveData<ResultLocalAudios> fetchLocalAudio();
    MutableLiveData<ResultLocalAudios> fetchLocalAudio(LocalAudio localAudio);
    void updateLocalAudio(LocalAudio localAudio);
}
