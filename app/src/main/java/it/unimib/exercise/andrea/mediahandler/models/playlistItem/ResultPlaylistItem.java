package it.unimib.exercise.andrea.mediahandler.models.playlistItem;

import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public abstract class ResultPlaylistItem {
    private ResultPlaylistItem() {}

    public boolean isSuccess() {
        if (this instanceof ResultPlaylistItem.Success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends ResultPlaylistItem {
        private final PlaylistItemApiResponse playlistItemApiResponse;
        public Success(PlaylistItemApiResponse playlistItemApiResponse) {
            this.playlistItemApiResponse = playlistItemApiResponse;
        }
        public PlaylistItemApiResponse getData() {
            return playlistItemApiResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends ResultPlaylistItem {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
