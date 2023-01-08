package it.unimib.exercise.andrea.mediahandler.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;
import it.unimib.exercise.andrea.mediahandler.repository.IMediaRepository;

public class ViewModelMedia extends ViewModel {
    private MutableLiveData<ResultLocalVideos> localVideosLiveData;
    private MutableLiveData<ResultLocalVideos> localVideoSavedLiveData;
    private MutableLiveData<ResultLocalAudios> localAudiosLiveData;
    private MutableLiveData<ResultLocalAudios> localAudiosSavedLiveData;
    private final IMediaRepository mediaRepository;
    private static final String TAG = ViewModelMedia.class.getSimpleName();

    public ViewModelMedia(IMediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }


    public MutableLiveData<ResultLocalVideos> getLocalVideos(){
        fetchLocalVideos();
        return localVideosLiveData;
    }
    public MutableLiveData<ResultLocalVideos> getInsertLocalVideo(LocalVideo localVideo) {
        fetchInsertLocalVideo(localVideo);
        return  localVideoSavedLiveData;
    }
    public void updateLocalVideo(LocalVideo localVideo){
        mediaRepository.updateLocalVideo(localVideo);
    }


    public MutableLiveData<ResultLocalAudios> getLocalAudio(){
        fetchLocalAudios();
        return localAudiosLiveData;
    }
    public MutableLiveData<ResultLocalAudios> getInsertLocalAudio(LocalAudio localAudio) {
        Log.d(TAG, "getLocalAudio: ");
        fetchInsertLocalAudio(localAudio);
        return localAudiosSavedLiveData;
    }
    public void updateLocalAudio(LocalAudio localAudio){
        mediaRepository.updateLocalAudio(localAudio);
    }


    private void fetchLocalVideos() {
        localVideosLiveData = mediaRepository.fetchLocalVideo();
    }
    private void fetchInsertLocalVideo(LocalVideo localVideo) {
        localVideoSavedLiveData = mediaRepository.fetchInsertLocalVideo(localVideo);
    }


    private void fetchLocalAudios() {
        localAudiosLiveData = mediaRepository.fetchLocalAudio();
    }
    private void fetchInsertLocalAudio(LocalAudio localAudio) {
        localAudiosSavedLiveData = mediaRepository.fetchInsertLocalAudio(localAudio);
    }
}
