package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.API_KEY_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.RETROFIT_ERROR;

import androidx.annotation.NonNull;

import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.service.YoutubeApiService;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to get news from a remote source using Retrofit.
 */
public class PlaylistRemoteDataSource extends BasePlaylistRemoteDataSource {

    private final YoutubeApiService youtubeApiService;
    private final String apiKey;

    public PlaylistRemoteDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.youtubeApiService = ServiceLocator.getInstance().getNewsApiService();
    }

    @Override
    public void getPlaylist() {
        Call<PlaylistApiResponse> newsResponseCall = youtubeApiService.getPlaylists(apiKey);

        newsResponseCall.enqueue(new Callback<PlaylistApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistApiResponse> call,
                                   @NonNull Response<PlaylistApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    newsCallback.onSuccessFromRemote(response.body());

                } else {
                    newsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistApiResponse> call, @NonNull Throwable t) {
                newsCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }
}
