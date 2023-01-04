package it.unimib.exercise.andrea.mediahandler.models.playlists;

import androidx.annotation.NonNull;

import it.unimib.exercise.andrea.mediahandler.models.Result;

/**
 * Class that represents the result of an action that requires
 * the use of a Web Service or a local database.
 */
public abstract class ResultPlaylist implements Result {
    private ResultPlaylist() {}

    @Override
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
    public static final class Success extends ResultPlaylist {
        private final PlaylistApiResponse playlistApiResponse;
        public Success(PlaylistApiResponse playlistApiResponse) {
            this.playlistApiResponse = playlistApiResponse;
        }
        public PlaylistApiResponse getData() {
            return playlistApiResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends ResultPlaylist {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
