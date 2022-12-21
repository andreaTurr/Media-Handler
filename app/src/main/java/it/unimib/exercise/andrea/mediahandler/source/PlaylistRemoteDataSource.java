package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.API_KEY_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.RETROFIT_ERROR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.service.YoutubeApiService;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to get news from a remote source using Retrofit.
 */
public class PlaylistRemoteDataSource extends BasePlaylistRemoteDataSource {
    private static final String TAG = PlaylistRemoteDataSource.class.getSimpleName();
    private AuthorizationService mAuthService = null ;
    private AuthStateManager mStateManager = null;
    private final YoutubeApiService youtubeApiService;
    private final String apiKey;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Context context = null;

    public PlaylistRemoteDataSource(String apiKey, Context context) {
        this.apiKey = apiKey;
        this.youtubeApiService = ServiceLocator.getInstance().getYoutubeApiService();
        this.context = context;
    }

    public void setYoutubeAuthorizations(AuthorizationService mAuthService,
                                         AuthStateManager mStateManager){
        this.mAuthService = mAuthService;
        this.mStateManager = mStateManager;
        if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
            Log.d(TAG, "onViewCreated: already authorized");
        }else{
            AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
            );
            String clientId = context.getString(R.string.client_id);
            Uri redirectUri = Uri.parse(context.getString(R.string.redirect_uri)) ;
            //clientId = "363443980550-0n72kud3dh999u8nuuf0p9n168371sni.apps.googleusercontent.com";
            //redirectUri = Uri.parse("com.googleusercontent.apps.363443980550-0n72kud3dh999u8nuuf0p9n168371sni:/oauth2redirect");
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfig,
                    clientId,
                    ResponseTypeValues.CODE,
                    redirectUri
            );
            Log.d(TAG, "onViewCreated: service config: " + serviceConfig +
                    " client_id:" + clientId +
                    " redirectUri:" + redirectUri);
            //builder.setScopes(String.valueOf(R.string.authorization_scope));
            builder.setScopes(String.valueOf("https://www.googleapis.com/auth/youtube"));

            AuthorizationRequest authRequest = builder.build() ;
            AuthorizationService authService = new AuthorizationService((Activity) context) ;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Intent authIntent = authService.getAuthorizationRequestIntent(authRequest) ;
                    authResponseActivityResultLauncher.launch(authIntent);
                }
            });

            Log.d(TAG, "End onViewCreate");
        }
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
