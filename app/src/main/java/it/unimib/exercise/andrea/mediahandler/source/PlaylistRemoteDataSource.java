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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
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

    public PlaylistRemoteDataSource(Context context,
                                    AuthStateManager mStateManager ) {
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
    public void getPlaylist() {

        /*if (mStateManager.getCurrent().getLastAuthorizationResponse().accessToken == null){
            return ;
        }*/
        Log.d(TAG, TAG + ": start ");
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
                    Call<PlaylistApiResponse> playlistApiResponse = youtubeApiService.getPlaylists(
                            String.format("Bearer %s", accessToken));
                    playlistApiResponse.enqueue(new Callback<PlaylistApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<PlaylistApiResponse> call,
                                               @NonNull Response<PlaylistApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                newsCallback.onSuccessFromRemote(response.body());
                            } else {
                                Log.d(TAG, "onResponse: " + response.raw() + "   " + accessToken );
                                newsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<PlaylistApiResponse> call, @NonNull Throwable t) {
                            String message = t.getMessage();
                            Log.d("failure", message);
                            newsCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
                        }
                    });
                }
            });
        }else{
            Log.d(TAG, "getPlaylist: mAuthService == null");
        }
    }
}
