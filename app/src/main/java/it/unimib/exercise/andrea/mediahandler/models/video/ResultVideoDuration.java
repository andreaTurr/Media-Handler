package it.unimib.exercise.andrea.mediahandler.models.video;

import it.unimib.exercise.andrea.mediahandler.models.Result;

public class ResultVideoDuration implements Result {
    private ResultVideoDuration() {}

    @Override
    public boolean isSuccess() {
        if (this instanceof ResultVideoDuration.Success) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends ResultVideoDuration {
        private final long totalPlaylistDuration;
        public Success(long totalPlaylistDuration) {
            this.totalPlaylistDuration = totalPlaylistDuration;
        }
        public long getData() {
            return totalPlaylistDuration;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends ResultVideoDuration {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
