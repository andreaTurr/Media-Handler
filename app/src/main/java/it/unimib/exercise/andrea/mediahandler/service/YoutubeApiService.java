package it.unimib.exercise.andrea.mediahandler.service;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.PLAYLIST_ITEMS_ENDPOINT;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.PLAYLIST_LIST_ENDPOINT;

import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface YoutubeApiService {
    @GET(PLAYLIST_LIST_ENDPOINT)
    Call<PlaylistApiResponse> getPlaylistsList(
            @Header("Authorization") String token);

    @GET(PLAYLIST_ITEMS_ENDPOINT)
    Call<it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse> getPlaylists(
            @Query("playlistId") String playlistId,
            @Header("Authorization") String token);
}
//  'https://youtube.googleapis.com/youtube/v3/
//  playlists?part=snippet%2CcontentDetails&maxResults=25&mine=true
//
//  &key=[YOUR_API_KEY]' \
//
//GET https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&playlistId=PLsizmByY7_uV0J7feYX4pta_05SV5SkJr&key=[YOUR_API_KEY] HTTP/1.1
//
//Authorization: Bearer [YOUR_ACCESS_TOKEN]
//Accept: application/json
