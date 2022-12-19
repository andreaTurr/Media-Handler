package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.API_KEY_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.PLAYLIST_API_TEST_JSON_FILE;

import java.io.IOException;

import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.util.JSONParserUtil;

public class PlaylistMockRemoteDataSource extends BasePlaylistRemoteDataSource {
    private final JSONParserUtil jsonParserUtil;

    public PlaylistMockRemoteDataSource(JSONParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getPlaylist() {
        PlaylistApiResponse playlistApiResponse = null;
        try {
            playlistApiResponse = jsonParserUtil.parseJSONFileWithGSon(PLAYLIST_API_TEST_JSON_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (playlistApiResponse != null) {
            newsCallback.onSuccessFromRemote(playlistApiResponse);
        } else {
            newsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
        }
    }
}
