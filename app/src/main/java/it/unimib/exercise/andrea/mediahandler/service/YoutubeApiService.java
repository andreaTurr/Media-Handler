package it.unimib.exercise.andrea.mediahandler.service;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.PLAYLIST_ENDPOINT;

import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface YoutubeApiService {
    @GET(PLAYLIST_ENDPOINT)
    Call<PlaylistApiResponse> getPlaylists(
            @Header("Authorization") String token);


}
//  'https://youtube.googleapis.com/youtube/v3/playlists?part=snippet%2CcontentDetails&maxResults=25&mine=true&key=[YOUR_API_KEY]' \
