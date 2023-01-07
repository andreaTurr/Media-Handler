package it.unimib.exercise.andrea.mediahandler.models.localAudio;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.Result;

public abstract class ResultLocalAudios implements Result {

    @Override
    public boolean isSuccess() {
        return this instanceof ResultLocalAudios.Success;
    }

    public final static class Success extends ResultLocalAudios {
        private final List<LocalAudio> localAudios;

        public Success(List<LocalAudio> localAudios) {
            this.localAudios = localAudios;
        }

        public  List<LocalAudio> getData(){return localAudios;}
    }
}
