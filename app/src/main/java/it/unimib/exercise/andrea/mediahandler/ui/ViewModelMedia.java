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


    public MutableLiveData<ResultLocalVideos> getLocalsVideo(){
        if(localVideosLiveData == null){
            fetchLocalVideos();
        }
        return localVideosLiveData;
    }
    public MutableLiveData<ResultLocalVideos> getLocalVideo(LocalVideo localVideo) {
        fetchLocalVideoCurrentTime(localVideo);
        return  localVideoSavedLiveData;
    }
    public void updateLocalVideo(LocalVideo localVideo){
        mediaRepository.updateLocalVideo(localVideo);
    }


    public MutableLiveData<ResultLocalAudios> getLocalAudio(){
        if(localVideosLiveData == null){
            fetchLocalAudios();
        }
        return localAudiosLiveData;
    }
    public MutableLiveData<ResultLocalAudios> getLocalAudio(LocalAudio localAudio) {
        Log.d(TAG, "getLocalAudio: ");
        fetchLocalAudioCurrentTime(localAudio);
        return localAudiosSavedLiveData;
    }
    public void updateLocalAudio(LocalAudio localAudio){
        mediaRepository.updateLocalAudio(localAudio);
    }


    private void fetchLocalVideos() {
        localVideosLiveData = mediaRepository.fetchLocalVideo();
    }
    private void fetchLocalVideoCurrentTime(LocalVideo localVideo) {
        localVideoSavedLiveData = mediaRepository.fetchLocalVideo(localVideo);
    }


    private void fetchLocalAudios() {
        localAudiosLiveData = mediaRepository.fetchLocalAudio();
    }
    private void fetchLocalAudioCurrentTime(LocalAudio localAudio) {
        localAudiosSavedLiveData = mediaRepository.fetchLocalAudio(localAudio);
    }
}
