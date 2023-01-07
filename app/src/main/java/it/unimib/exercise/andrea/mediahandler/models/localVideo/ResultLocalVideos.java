package it.unimib.exercise.andrea.mediahandler.models.localVideo;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.Result;

public abstract class ResultLocalVideos implements Result {

    @Override
    public boolean isSuccess() {
        return this instanceof ResultLocalVideos.Success;
    }

    public final static class Success extends ResultLocalVideos{
        private final List<LocalVideo> localVideos;

        public Success(List<LocalVideo> localVideos) {
            this.localVideos = localVideos;
        }

        public  List<LocalVideo> getData(){return localVideos;}
    }
}
