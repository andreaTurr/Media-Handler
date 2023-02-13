package it.unimib.exercise.andrea.mediahandler.repository.media;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.source.media.MediaCallback;
import it.unimib.exercise.andrea.mediahandler.source.media.MediaDataSource;

public class MediaRepository implements IMediaRepository, MediaCallback {
    private static final String TAG = MediaRepository.class.getSimpleName();
    private final MutableLiveData<Result> localVideosLiveData;
    private MutableLiveData<Result> localVideosCurrentTimeLiveData;
    private final MutableLiveData<Result> localAudiosLiveData;
    private MutableLiveData<Result> localAudiosCurrentTimeLiveData;

    private final MediaDataSource mediaDataSource;

    public MediaRepository(MediaDataSource mediaDataSource) {
        this.mediaDataSource = mediaDataSource;
        this.localAudiosLiveData = new MutableLiveData<>();
        this.localVideosLiveData = new MutableLiveData<>();
        this.mediaDataSource.setPlaylistCallback(this);
    }

    @Override
    public MutableLiveData<Result> fetchLocalVideo() {
        mediaDataSource.getVideos();
        return localVideosLiveData;
    }

    @Override
    public MutableLiveData<Result> fetchInsertLocalVideo(LocalVideo localVideo) {
        localVideosCurrentTimeLiveData = new MutableLiveData<>();
        mediaDataSource.getInsertLocalVideo(localVideo);
        return localVideosCurrentTimeLiveData;
    }

    @Override
    public void updateLocalVideo(LocalVideo localVideo) {
        mediaDataSource.updateLocalVideo(localVideo);
    }

    @Override
    public MutableLiveData<Result> fetchLocalAudio() {
        mediaDataSource.getAudios();
        return localAudiosLiveData;
    }

    @Override
    public MutableLiveData<Result> fetchInsertLocalAudio(LocalAudio localAudio) {
        Log.d(TAG, "fetchLocalAudio: ");
        localAudiosCurrentTimeLiveData = new MutableLiveData<>();
        mediaDataSource.getInsertLocalAudio(localAudio);
        return localAudiosCurrentTimeLiveData;
    }

    @Override
    public void updateLocalAudio(LocalAudio localAudio) {
        mediaDataSource.updateLocalAudio(localAudio);
    }

    //callbacks

    @Override
    public void onSuccessFromStorageVideo(Result localVideos) {
        localVideosLiveData.postValue(localVideos);
    }

    @Override
    public void onSuccessFromGetVideoCurrentTime(Result localVideo) {
        localVideosCurrentTimeLiveData.postValue(localVideo);
    }

    @Override
    public void onSuccessFromStorageAudio(Result localAudios) {
        localAudiosLiveData.postValue(localAudios);
    }

    @Override
    public void onSuccessFromGetAudioCurrentTime(Result localAudio) {
        localAudiosCurrentTimeLiveData.postValue(localAudio);
    }
}
