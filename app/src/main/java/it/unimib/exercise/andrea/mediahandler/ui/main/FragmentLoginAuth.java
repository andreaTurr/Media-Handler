package it.unimib.exercise.andrea.mediahandler.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import it.unimib.exercise.andrea.mediahandler.R ;
import it.unimib.exercise.andrea.mediahandler.databinding.FragmentLoginAuthBinding;
import it.unimib.exercise.andrea.mediahandler.util.AuthStateManager;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
    private FragmentLoginAuthBinding fragmentLoginAuthBinding;

    public FragmentLoginAuth() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mStateManager = AuthStateManager.getInstance(getActivity()) ;
        mStateManager = AuthStateManager.getInstance(getActivity()) ;
        //mAuthService = new AuthorizationService(getActivity()) ;

        if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
            Log.d(TAG, "onViewCreated: already authorized");
            //Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLoginAuth_to_fragmentPlaylistList);
        }else{
            AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
            );
            String clientId = getString(R.string.client_id) ;
            Uri redirectUri = Uri.parse(getString(R.string.redirect_uri)) ;
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

            // -------------------------------------------------------------------------------------
            // Step 2. Authorizing the user, via a browser, in order to obtain an authorization
            // code.
            // -------------------------------------------------------------------------------------
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Intent authIntent = authService.getAuthorizationRequestIntent(authRequest) ;
                    authResponseActivityResultLauncher.launch(authIntent);
                    /*authService.performAuthorizationRequest(
                            authRequest,
                            PendingIntent.getActivity(getContext(), 0,
                                    new Intent(getContext(), FragmentPlayListList.class), 0), //completed intent
                            PendingIntent.getActivity(getContext(), 0,
                                    new Intent(getContext(), FragmentLoginAuth.class), 0));   //cancelled intent  */
                }
            });
            Log.d(TAG, "End btnFragAuth.setOnClickListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnFragAuth = view.findViewById(R.id.bttnFragAuth) ;
        btnFragAuth.setOnClickListener(view1 -> {
            // -------------------------------------------------------------------------------------
            // OAuth2 Step 1. Discovering or specifying the endpoints to interact with the provider.
            // -------------------------------------------------------------------------------------
            if (mStateManager != null && mStateManager.getCurrent().isAuthorized()) {
                Log.d(TAG, "onViewCreated: already authorized");
            }else{
                AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                        Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
                        Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
                );
                String clientId = getString(R.string.client_id) ;
                Uri redirectUri = Uri.parse(getString(R.string.redirect_uri)) ;
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

                // -------------------------------------------------------------------------------------
                // Step 2. Authorizing the user, via a browser, in order to obtain an authorization
                // code.
                // -------------------------------------------------------------------------------------
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest) ;
                        authResponseActivityResultLauncher.launch(authIntent);
                        /*authService.performAuthorizationRequest(
                                authRequest,
                                PendingIntent.getActivity(getContext(), 0,
                                        new Intent(getContext(), FragmentPlayListList.class), 0), //completed intent
                                PendingIntent.getActivity(getContext(), 0,
                                        new Intent(getContext(), FragmentLoginAuth.class), 0));   //cancelled intent*/
                    }
                });


                Log.d(TAG, "End btnFragAuth.setOnClickListener");
            }
        });

        if (mStateManager != null && mStateManager.getCurrent().isAuthorized()){
            Log.d(TAG, "Auth Done") ;
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentLoginAuth_to_fragmentPlaylistList);
        }else{
            Log.d(TAG, "Auth not Done " + mStateManager.getCurrent().isAuthorized()) ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login_auth, container, false);
        fragmentLoginAuthBinding = FragmentLoginAuthBinding.inflate(inflater, container, false);
        return fragmentLoginAuthBinding.getRoot();
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
                                                Navigation.findNavController(requireView()).navigate(
                                                        R.id.action_fragmentLoginAuth_to_fragmentPlaylistList);
                                                /*executorService.execute(new Runnable() {
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
                                                });*/
                                            } else {
                                                // authorization failed, check ex for more details
                                            }
                                        }
                                    });
                        }
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