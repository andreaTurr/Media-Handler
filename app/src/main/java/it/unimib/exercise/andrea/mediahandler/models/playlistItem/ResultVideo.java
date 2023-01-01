package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

public abstract class ResultVideo {
    private ResultVideo() {}

    public boolean isSuccess() {
        if (this instanceof ResultVideo.Success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends ResultVideo {
        private final Video video;
        public Success(Video video) {
            this.video = video;
        }
        public Video getData() {
            return video;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends ResultVideo {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
