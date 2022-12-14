package it.unimib.exercise.andrea.mediahandler.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import it.unimib.exercise.andrea.mediahandler.R ;
import it.unimib.exercise.andrea.mediahandler.service.AuthStateManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentLoginAuth extends Fragment {
    public static final String TAG =  FragmentLoginAuth.class.getSimpleName();
    private AuthorizationService mAuthService = null ;
    private AuthStateManager mStateManager = null;
    private Button bttnFragAuth = null ;

    public FragmentLoginAuth() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
                Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
        );
        String clientId = String.valueOf(R.string.client_id);
        Uri redirectUri = Uri.parse(String.valueOf(R.string.redirect_uri)) ;
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfig,
                clientId,
                ResponseTypeValues.CODE,
                redirectUri
        );
        builder.setScopes(String.valueOf(R.string.authorization_scope));

        AuthorizationRequest authRequest = builder.build() ;
        AuthorizationService authService = new AuthorizationService(getActivity()) ;
        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest) ;
        someActivityResultLauncher.launch(authIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "ActivityResult: " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        AuthorizationResponse resp = AuthorizationResponse.fromIntent(result.getData());

                        AuthorizationException ex = AuthorizationException.fromIntent(result.getData());
                        // … process the response or exception …

                        mAuthService.performTokenRequest(
                                resp.createTokenExchangeRequest(),
                                new AuthorizationService.TokenResponseCallback() {
                                    @Override public void onTokenRequestCompleted(
                                            TokenResponse resp, AuthorizationException ex) {
                                        if (resp != null) {
                                            // exchange succeeded
                                            mStateManager.updateAfterTokenResponse(resp, ex) ;
                                            Log.d("accessToken",resp.accessToken) ;
                                        } else {
                                            // authorization failed, check ex for more details
                                        }
                                    }
                                });

                    }

                    if (mStateManager.getCurrent().isAuthorized()) {

                        mStateManager.getCurrent().performActionWithFreshTokens(mAuthService, new AuthState.AuthStateAction() {
                            @Override public void execute(
                                    String accessToken,
                                    String idToken,
                                    AuthorizationException ex) {
                                if (ex != null) {
                                    // negotiation for fresh tokens failed, check ex for more details
                                    ProfileTask.execute(accessToken) ;
                                    return;
                                }

                                // use the access token to do something ...
                            }
                        });


                    }
                }
            });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnFragAuth = view.findViewById(R.id.bttnFragAuth) ;
        btnFragAuth.setOnClickListener(view1 -> {

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_auth, container, false);
    }

    static class ProfileTask extends AsyncTask<String, Void, JSONObject>  {


        @Override
        protected JSONObject doInBackground(String... tokens) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v3/userinfo")
                    .addHeader("Authorization", String.format("Bearer %s", tokens[0]))
                    .build() ;
            try {
                Response response = client.newCall(request).execute();
                String jsonBody  = response.body().string() ;
                Log.i("LOG_TAG", String.format("User Info Response %s", jsonBody)) ;
                return new JSONObject(jsonBody) ;
            }catch (Exception e) {
                Log.w("LOG_TAG", e);
            }
            return null;
        }

        /*@Override
        public void onPostExecute(JSONObject userInfo) {
            if (userInfo != null) {
                String fullName = userInfo.optString("name", null) ;
                String imageUrl =
                        userInfo.optString("picture", null);
                if (!TextUtils.isEmpty(imageUrl)) {
                    Glide.with(getActivity()).load(imageUrl).into(imgProfile);
                }
                if (!TextUtils.isEmpty(fullName)) {
                    textUsername.setText(fullName)
                }
                String message ;
                if (userInfo.has("error")) {
                    message = java.lang.String.format(
                        "%s [%s]", "R.string.request_failed", userInfo.optString("error_description", "No description"));
                } else {
                    message = "R.string.request_complete" ;
                }
                Snackbar.make(imgProfile, message, Snackbar.LENGTH_SHORT).show()
            }
        }*/


    }

}