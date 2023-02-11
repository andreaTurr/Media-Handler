package it.unimib.exercise.andrea.mediahandler.repository.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Set;

import it.unimib.exercise.andrea.mediahandler.models.Result;
import it.unimib.exercise.andrea.mediahandler.models.User;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.source.playlist.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.PlaylistCallback;
import it.unimib.exercise.andrea.mediahandler.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.user.BaseUserDataRemoteDataSource;

/**
 * Repository class to get the user information.
 */
public class UserRepository implements IUserRepository, UserResponseCallback, PlaylistCallback  {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BasePlaylistLocalDataSource playlistLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userPlaylistsMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BasePlaylistLocalDataSource playlistLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.playlistLocalDataSource = playlistLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userPlaylistsMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.playlistLocalDataSource.setPlaylistCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserPlaylists(String idToken) {
        userDataRemoteDataSource.getUserPlaylists(idToken);
        return userPlaylistsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserPreferences(String idToken) {
        userDataRemoteDataSource.getUserPreferences(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken) {
        userDataRemoteDataSource.saveUserPreferences(favoriteCountry, favoriteTopics, idToken);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Playlist> playlistList) {
        playlistLocalDataSource.insertPlaylistsList(new PlaylistApiResponse(playlistList));
    }

    @Override
    public void onSuccessFromGettingUserPreferences() {
        userPreferencesMutableLiveData.postValue(new Result.UserResponseSuccess(null));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        playlistLocalDataSource.deleteAll();
    }


    @Override
    public void onSuccessFromRemotePlaylistList(PlaylistApiResponse newsApiResponse) {

    }

    @Override
    public void onFailureFromRemotePlaylistList(Exception exception) {

    }

    @Override
    public void onSuccessFromLocalPlaylistList(PlaylistApiResponse playlistApiResponse) {
        //todo psta result del giro che recupera i minutaggi
    }

    @Override
    public void onFailureFromLocalPlaylistList(Exception exception) {

    }

    @Override
    public void onSuccessFromLocalLastUpdate(Long lastUpdate, String playlistId) {

    }

    @Override
    public void onSuccessFromRemoteVideoList(PlaylistItemApiResponse response, String playlistId) {

    }

    @Override
    public void onFailureFromRemoteVideoList(Exception exception) {

    }

    @Override
    public void onSuccessFromLocalVideoList(PlaylistItemApiResponse playlistItemApiResponse) {

    }

    @Override
    public void onFailureFromLocalVideoList(Exception exception) {

    }

    @Override
    public void onSuccessFromLocalVideo(Video video) {

    }

    @Override
    public void onFailureFromLocalVideo(Exception exception) {

    }

    @Override
    public void onSuccesFromRemotePlaylistDuration(long duration) {

    }

    @Override
    public void onSuccessDeletion() {
        Log.d(TAG, "onSuccessDeletion: ");
        userMutableLiveData.postValue(new Result.UserResponseSuccess(null));
    }
}
