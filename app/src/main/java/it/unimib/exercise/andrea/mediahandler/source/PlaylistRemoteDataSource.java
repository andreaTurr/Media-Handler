package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.API_KEY_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.RETROFIT_ERROR;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.video.VideoApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.video.VideoDetailed;
import it.unimib.exercise.andrea.mediahandler.service.YoutubeApiService;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to get playlist from a remote source using Retrofit.
 */
public class PlaylistRemoteDataSource extends BasePlaylistRemoteDataSource {
    private static final String TAG = PlaylistRemoteDataSource.class.getSimpleName();
    private AuthStateManager mStateManager ;
    private final YoutubeApiService youtubeApiService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Context context = null;
    AuthorizationService mAuthService ;
    private final String apiKey;

    public PlaylistRemoteDataSource(Context context, AuthStateManager mStateManager, String apiKey) {
        this.apiKey = apiKey;
        this.youtubeApiService = ServiceLocator.getInstance().getYoutubeApiService();
        this.context = context;
        this.mStateManager = mStateManager ;
        mAuthService = new AuthorizationService(context);
        getAuthToken();
    }

    private void getAuthToken(){
        // -----------------------------------------------------------------------------------------
        // Oauth2 Step 3. Exchanging the authorization code with the authorization server, to obtain a refresh
        // token and/or ID token.
        // -----------------------------------------------------------------------------------------
        Log.d(TAG, "getAuthToken: start");
        if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
            Log.d(TAG, "getAuthToken: already authorized");
            //mStateManager.getCurrent().update(mStateManager.getCurrent().getLastRegistrationResponse());
        }else if (mStateManager != null && mAuthService != null){
            mAuthService.performTokenRequest(
                    //resp.createTokenExchangeRequest(),
                    mStateManager.getCurrent().getLastAuthorizationResponse().createTokenExchangeRequest(),
                    new AuthorizationService.TokenResponseCallback() {
                        @Override public void onTokenRequestCompleted(
                                TokenResponse resp, AuthorizationException ex) {
                            if (resp != null) {
                                // exchange succeeded
                                mStateManager.updateAfterTokenResponse(resp, ex) ;
                                Log.d(TAG,"onTokenRequestCompleted: accessToken" + resp.accessToken) ;
                            } else {
                                // authorization failed, check ex for more details
                                Log.d(TAG, "onTokenRequestCompleted: auth failed");
                            }
                        }
                    });
        }else{
            Log.d(TAG, "getAuthToken: mAuthService == null or mStateManager == null");
        }
        
    }


    @Override
    public void getPlaylistList() {

        /*if (mStateManager.getCurrent().getLastAuthorizationResponse().accessToken == null){
            return ;
        }*/
        Log.d(TAG, "getPlaylistList remote");
        if (mAuthService != null) {
            //--------------------------------------------------------------------------------------
            //  Oauth2 Step 4. Using access tokens derived from the refresh token to interact with a
            //  resource server for further access to user data.
            //--------------------------------------------------------------------------------------
            mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                @Override
                public void execute(@Nullable String accessToken,
                                    @Nullable String idToken,
                                    @Nullable AuthorizationException ex) {
                    Call<PlaylistApiResponse> playlistApiResponse = youtubeApiService.getPlaylistsList(
                            String.format("Bearer %s", accessToken));
                    playlistApiResponse.enqueue(new Callback<PlaylistApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<PlaylistApiResponse> call,
                                               @NonNull Response<PlaylistApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                playlistCallback.onSuccessFromRemotePlaylistList(response.body());
                            } else {
                                playlistCallback.onFailureFromRemotePlaylistList(new Exception(API_KEY_ERROR));
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<PlaylistApiResponse> call, @NonNull Throwable t) {
                            String message = t.getMessage();
                            Log.d("failure", message);
                            playlistCallback.onFailureFromRemotePlaylistList(new Exception(RETROFIT_ERROR));
                        }
                    });
                }
            });
        }else{
            Log.d(TAG, "getPlaylistList: mAuthService == null");
        }
    }

    @Override
    public void getVideoList(String playlistId) {
        Log.d(TAG, "getVideoList: Remote");
        if (mAuthService != null) {
            //--------------------------------------------------------------------------------------
            //  Oauth2 Step 4. Using access tokens derived from the refresh token to interact with a
            //  resource server for further access to user data.
            //--------------------------------------------------------------------------------------
            mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                @Override
                public void execute(@Nullable String accessToken,
                                    @Nullable String idToken,
                                    @Nullable AuthorizationException ex) {
                    Call<PlaylistItemApiResponse> playlistItemApiResponseCall = youtubeApiService.getPlaylists(
                            playlistId, String.format("Bearer %s", accessToken) );
                    playlistItemApiResponseCall.enqueue(new Callback<PlaylistItemApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<PlaylistItemApiResponse> call,
                                               @NonNull Response<PlaylistItemApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                playlistCallback.onSuccessFromRemoteVideoList(response.body(), playlistId);
                            } else {
                                playlistCallback.onFailureFromRemotePlaylistList(
                                        new Exception(API_KEY_ERROR));
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<PlaylistItemApiResponse> call, @NonNull Throwable t) {
                            String message = t.getMessage();
                            Log.d("failure", message);
                            playlistCallback.onFailureFromRemotePlaylistList(
                                    new Exception(RETROFIT_ERROR));
                        }
                    });
                }
            });
        }else{
            Log.d(TAG, "getPlaylist: mAuthService == null");
        }
    }


    @Override
    public void getPlaylistDuration(String playlistId) {
        Log.d(TAG, "getPlaylistDuration: Remote");
        if (mAuthService != null) {
            //--------------------------------------------------------------------------------------
            //  Oauth2 Step 4. Using access tokens derived from the refresh token to interact with a
            //  resource server for further access to user data.
            //--------------------------------------------------------------------------------------
            Call<VideoApiResponse> playlistItemApiResponseCall = youtubeApiService.getVideoDetailed(
                    playlistId, apiKey);
            playlistItemApiResponseCall.enqueue(new Callback<VideoApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<VideoApiResponse> call,
                                       @NonNull Response<VideoApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        //playlistCallback.onSuccessFromRemoteVideoList(response.body(), playlistId);
                        VideoApiResponse responseApi = response.body();
                        List<VideoDetailed> videoDetailedList = responseApi.getVideoDetailedList();
                        long duration = 0;
                        /*while(!videoDetailedList.isEmpty()){
                            duration += getDuration(videoDetailedList.get(0).getVideoDetailedContentDetails().getDuration());
                            videoDetailedList.remove(0);
                        }*/
                        for (VideoDetailed video: videoDetailedList) {
                            duration += getDuration(video.getVideoDetailedContentDetails().getDuration());
                            Log.d(TAG, "onResponse: " + duration);
                        }
                        playlistCallback.onSuccesFromRemotePlaylistDuration(duration);
                    } else {
                        //playlistCallback.onFailureFromRemotePlaylistList(
                        //        new Exception(API_KEY_ERROR));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VideoApiResponse> call, @NonNull Throwable t) {
                    String message = t.getMessage();
                    Log.d("failure", message);
                    //playlistCallback.onFailureFromRemotePlaylistList(
                    //        new Exception(RETROFIT_ERROR));
                }
            });

        }else{
            Log.d(TAG, "getPlaylistDuration: mAuthService == null");
        }
    }


    public long getDuration(String time) {
        Log.d(TAG, "getDuration: " + time);
        Duration duration = Duration.parse(time);
        return duration.getSeconds();
    }
}
