package it.unimib.exercise.andrea.mediahandler.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.repository.media.IMediaRepository;

public class ViewModelMedia extends ViewModel {
    private MutableLiveData<Result> localVideosLiveData;
    private MutableLiveData<Result> localVideoSavedLiveData;
    private MutableLiveData<Result> localAudiosLiveData;
    private MutableLiveData<Result> localAudiosSavedLiveData;
    private final IMediaRepository mediaRepository;
    private static final String TAG = ViewModelMedia.class.getSimpleName();

    public ViewModelMedia(IMediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }


    public MutableLiveData<Result> getLocalVideos(){
        fetchLocalVideos();
        return localVideosLiveData;
    }
    public MutableLiveData<Result> getInsertLocalVideo(LocalVideo localVideo) {
        fetchInsertLocalVideo(localVideo);
        return  localVideoSavedLiveData;
    }
    public void updateLocalVideo(LocalVideo localVideo){
        mediaRepository.updateLocalVideo(localVideo);
    }


    public MutableLiveData<Result> getLocalAudio(){
        fetchLocalAudios();
        return localAudiosLiveData;
    }
    public MutableLiveData<Result> getInsertLocalAudio(LocalAudio localAudio) {
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
