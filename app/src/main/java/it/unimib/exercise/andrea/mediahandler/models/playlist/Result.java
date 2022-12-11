package it.unimib.exercise.andrea.mediahandler.models.playlist;

/**
 * Class that represents the result of an action that requires
 * the use of a Web Service or a local database.
 */
public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        if (this instanceof Success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends Result {
        private final PlaylistApiResponse newsResponse;
        public Success(PlaylistApiResponse newsResponse) {
            this.newsResponse = newsResponse;
        }
        public PlaylistApiResponse getData() {
            return newsResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
