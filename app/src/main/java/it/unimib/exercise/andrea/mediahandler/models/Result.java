package it.unimib.exercise.andrea.mediahandler.models;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

public abstract class Result {
    public boolean isSuccess() {
        if (this instanceof ResultVideoDurationSuccess || this instanceof ResultPlaylistSuccess ||
                this instanceof ResultVideoSuccess || this instanceof ResultLocalVideosSuccess ||
                this instanceof ResultLocalAudiosSuccess || this instanceof ResultPlaylistItemSuccess ||
                this instanceof UserResponseSuccess || this instanceof GenericSuccess) {
            return true;
        } else {
            return false;
        }
    }

    public static final class ResultVideoDurationSuccess extends Result {
        private final long totalPlaylistDuration;
        public ResultVideoDurationSuccess(long totalPlaylistDuration) {
            this.totalPlaylistDuration = totalPlaylistDuration;
        }
        public long getData() {
            return totalPlaylistDuration;
        }
    }

    public static final class ResultPlaylistSuccess extends Result {
        private final PlaylistApiResponse playlistApiResponse;
        public ResultPlaylistSuccess(PlaylistApiResponse playlistApiResponse) {
            this.playlistApiResponse = playlistApiResponse;
        }
        public PlaylistApiResponse getData() {
            return playlistApiResponse;
        }
    }

    public static final class ResultVideoSuccess extends Result {
        private final Video video;
        public ResultVideoSuccess(Video video) {
            this.video = video;
        }
        public Video getData() {
            return video;
        }
    }

    public final static class ResultLocalVideosSuccess extends Result {
        private final List<LocalVideo> localVideos;

        public ResultLocalVideosSuccess(List<LocalVideo> localVideos) {
            this.localVideos = localVideos;
        }

        public  List<LocalVideo> getData(){return localVideos;}
    }

    public final static class ResultLocalAudiosSuccess extends Result {
        private final List<LocalAudio> localAudios;

        public ResultLocalAudiosSuccess(List<LocalAudio> localAudios) {
            this.localAudios = localAudios;
        }

        public  List<LocalAudio> getData(){return localAudios;}
    }

    public static final class ResultPlaylistItemSuccess extends Result {
        private final PlaylistItemApiResponse playlistItemApiResponse;
        public ResultPlaylistItemSuccess(PlaylistItemApiResponse playlistItemApiResponse) {
            this.playlistItemApiResponse = playlistItemApiResponse;
        }
        public PlaylistItemApiResponse getData() {
            return playlistItemApiResponse;
        }
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;
        public UserResponseSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class GenericSuccess extends Result {
        public GenericSuccess() {}
    }

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
