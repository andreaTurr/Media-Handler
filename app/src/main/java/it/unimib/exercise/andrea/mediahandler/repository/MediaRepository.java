package it.unimib.exercise.andrea.mediahandler.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;
import it.unimib.exercise.andrea.mediahandler.source.media.MediaCallback;
import it.unimib.exercise.andrea.mediahandler.source.media.MediaDataSource;

public class MediaRepository implements IMediaRepository, MediaCallback {
    private static final String TAG = MediaRepository.class.getSimpleName();
    private final MutableLiveData<ResultLocalVideos> localVideosLiveData;
    private MutableLiveData<ResultLocalVideos> localVideosCurrentTimeLiveData;
    private final MutableLiveData<ResultLocalAudios> localAudiosLiveData;
    private MutableLiveData<ResultLocalAudios> localAudiosCurrentTimeLiveData;

    private final MediaDataSource mediaDataSource;

    public MediaRepository(MediaDataSource mediaDataSource) {
        this.mediaDataSource = mediaDataSource;
        this.localAudiosLiveData = new MutableLiveData<>();
        this.localVideosLiveData = new MutableLiveData<>();
        this.mediaDataSource.setPlaylistCallback(this);

    }

    @Override
    public MutableLiveData<ResultLocalVideos> fetchLocalVideo() {
        mediaDataSource.getVideos();
        return localVideosLiveData;
    }

    @Override
    public MutableLiveData<ResultLocalVideos> fetchInsertLocalVideo(LocalVideo localVideo) {
        localVideosCurrentTimeLiveData = new MutableLiveData<>();
        mediaDataSource.getInsertLocalVideo(localVideo);
        return localVideosCurrentTimeLiveData;
    }

    @Override
    public void updateLocalVideo(LocalVideo localVideo) {
        mediaDataSource.updateLocalVideo(localVideo);
    }

    @Override
    public MutableLiveData<ResultLocalAudios> fetchLocalAudio() {
        mediaDataSource.getAudios();
        return localAudiosLiveData;
    }

    @Override
    public MutableLiveData<ResultLocalAudios> fetchInsertLocalAudio(LocalAudio localAudio) {
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
    public void onSuccessFromStorageVideo(ResultLocalVideos localVideos) {
        localVideosLiveData.postValue(localVideos);
    }

    @Override
    public void onSuccessFromGetVideoCurrentTime(ResultLocalVideos localVideo) {
        localVideosCurrentTimeLiveData.postValue(localVideo);
    }

    @Override
    public void onSuccessFromStorageAudio(ResultLocalAudios localAudios) {
        localAudiosLiveData.postValue(localAudios);
    }

    @Override
    public void onSuccessFromGetAudioCurrentTime(ResultLocalAudios localAudio) {
        localAudiosCurrentTimeLiveData.postValue(localAudio);
    }
}
