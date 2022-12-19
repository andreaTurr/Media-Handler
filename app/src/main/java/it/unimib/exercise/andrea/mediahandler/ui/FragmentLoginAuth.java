package it.unimib.exercise.andrea.mediahandler.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import it.unimib.exercise.andrea.mediahandler.R ;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.EndSessionRequest;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLoginAuth extends Fragment {
    public static final String TAG =  FragmentLoginAuth.class.getSimpleName();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AuthorizationService mAuthService = null ;
    private AuthStateManager mStateManager = null;
    private Button bttnFragAuth = null ;
    private JSONObject objectJSON = null ;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    public FragmentLoginAuth() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateManager = AuthStateManager.getInstance(getActivity()) ;
        mAuthService = new AuthorizationService(getActivity()) ;

        if (mStateManager != null && mStateManager.getCurrent().isAuthorized()){
            Log.d(TAG, "Auth Done") ;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnFragAuth = view.findViewById(R.id.bttnFragAuth) ;
        Button bttToken = view.findViewById(R.id.bttnToken);
        Button bttLogOut = view.findViewById(R.id.bttnLogOut);
        btnFragAuth.setOnClickListener(view1 -> {

            if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
                Log.d(TAG, "onViewCreated: already authorized");
            }else{
                AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                        Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
                        Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
                );
                String clientId = String.valueOf(R.string.client_id);
                Uri redirectUri = Uri.parse(String.valueOf(R.string.redirect_uri)) ;
                clientId = "363443980550-0n72kud3dh999u8nuuf0p9n168371sni.apps.googleusercontent.com";
                redirectUri = Uri.parse("com.googleusercontent.apps.363443980550-0n72kud3dh999u8nuuf0p9n168371sni:/oauth2redirect");
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
                AuthorizationService authService = new AuthorizationService(getActivity()) ;
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest) ;
                        authResponseActivityResultLauncher.launch(authIntent);
                    }
                });

                Log.d(TAG, "End onViewCreate");
            }
        });
        bttToken.setOnClickListener(view1 -> {
            Log.d(TAG, "onViewCreated: bttnToken");
            if (mAuthService != null){
                mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                    @Override
                    public void execute(@Nullable String accessToken,
                                        @Nullable String idToken,
                                        @Nullable AuthorizationException ex) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "onCreate run");
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("https://youtube.googleapis.com/youtube/v3/playlists?part=snippet%2CcontentDetails&maxResults=25&mine=true") //  'https://youtube.googleapis.com/youtube/v3/playlists?part=snippet%2CcontentDetails&maxResults=25&mine=true&key=[YOUR_API_KEY]' \
                                        .addHeader("Authorization", String.format("Bearer %s", accessToken))
                                        .build() ;
                                try {
                                    Response response = client.newCall(request).execute();
                                    String jsonBody  = response.body().string() ;
                                    Log.i(TAG, String.format("User Info Response %s", jsonBody)) ;
                                    objectJSON = new JSONObject(jsonBody) ;
                                }catch (Exception e) {
                                    Log.w(TAG, e);
                                }
                            }
                        });
                    }
                });

            }else{
                Log.d(TAG, "onViewCreated: no auth avaiable");
            }
        });
        bttLogOut.setOnClickListener(view1 -> endSession());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_auth, container, false);
    }



    ActivityResultLauncher<Intent> authResponseActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "ActivityResult: " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        AuthorizationResponse resp = AuthorizationResponse.fromIntent(result.getData());
                        AuthorizationException ex = AuthorizationException.fromIntent(result.getData());
                        // … process the response or exception …
                        if (resp != null){
                            Log.d(TAG, "onActivityResult: save mAuth");
                            mAuthService = new AuthorizationService(getActivity());
                            mStateManager.updateAfterAuthorization(resp, ex);
                            mAuthService.performTokenRequest(
                                    resp.createTokenExchangeRequest(),
                                    new AuthorizationService.TokenResponseCallback() {
                                        @Override public void onTokenRequestCompleted(
                                                TokenResponse resp, AuthorizationException ex) {
                                            if (resp != null) {
                                                // exchange succeeded
                                                mStateManager.updateAfterTokenResponse(resp, ex) ;
                                                Log.d(TAG,"accessToken" + resp.accessToken) ;
                                                executorService.execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        OkHttpClient client = new OkHttpClient();
                                                        Request request = new Request.Builder()
                                                                .url("https://www.googleapis.com/youtube/v3/playlists?mine=true")
                                                                .addHeader("Authorization", String.format("Bearer %s", resp.accessToken))
                                                                .build() ;
                                                        try {
                                                            Response response = client.newCall(request).execute();
                                                            String jsonBody  = response.body().string() ;
                                                            Log.i(TAG, String.format("User Info Response %s", jsonBody)) ;
                                                            objectJSON = new JSONObject(jsonBody) ;
                                                        }catch (Exception e) {
                                                            Log.w(TAG, e);
                                                        }
                                                    }
                                                });
                                            } else {
                                                // authorization failed, check ex for more details
                                            }
                                        }
                                    });
                        }
                        /*if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
                            Log.d(TAG, "onActivityResult: perform userinfo?");
                            mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                                @Override public void execute(
                                        String accessToken,
                                        String idToken,
                                        AuthorizationException ex) {
                                    if (ex != null) {
                                        // negotiation for fresh tokens failed, check ex for more details
                                        //ProfileTask.execute(accessToken) ;
                                        executorService.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                OkHttpClient client = new OkHttpClient();
                                                Request request = new Request.Builder()
                                                        .url("https://www.googleapis.com/oauth2/v3/userinfo")
                                                        .addHeader("Authorization", String.format("Bearer %s", accessToken))
                                                        .build() ;
                                                try {
                                                    Response response = client.newCall(request).execute();
                                                    String jsonBody  = response.body().string() ;
                                                    Log.i(TAG, String.format("User Info Response %s", jsonBody)) ;
                                                    objectJSON = new JSONObject(jsonBody) ;
                                                }catch (Exception e) {
                                                    Log.w(TAG, e);
                                                }
                                            }
                                        });
                                        return;
                                    }
                                    // use the access token to do something ...
                                }
                            });
                        }*/
                    }
                }
            });



    @MainThread
    private void endSession() {
        AuthState currentState = mStateManager.getCurrent();
        AuthorizationServiceConfiguration config =
                currentState.getAuthorizationServiceConfiguration();
        if (config.endSessionEndpoint != null) {
            Intent endSessionIntent = mAuthService.getEndSessionRequestIntent(
                    new EndSessionRequest.Builder(config)
                            .setIdTokenHint(currentState.getIdToken())
                            .setPostLogoutRedirectUri(Uri.parse("it.unimib.exercise.andrea.mediahandler:/oauth2redirect"))
                            .build());
            endSessionActivityResultLauncher.launch(endSessionIntent);
        } else {
            signOut();
        }
    }

    ActivityResultLauncher<Intent> endSessionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "ActivityResult: " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        signOut();
                    }
                }
            });

    @MainThread
    private void signOut() {
        // discard the authorization and token state, but retain the configuration and
        // dynamic client registration (if applicable), to save from retrieving them again.
        AuthState currentState = mStateManager.getCurrent();
        AuthState clearedState =
                new AuthState(currentState.getAuthorizationServiceConfiguration());
        if (currentState.getLastRegistrationResponse() != null) {
            clearedState.update(currentState.getLastRegistrationResponse());
        }
        mStateManager.replace(clearedState);
        Log.d(TAG, "signOut: ");
    }

}