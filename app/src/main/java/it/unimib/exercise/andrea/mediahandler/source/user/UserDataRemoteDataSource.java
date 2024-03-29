package it.unimib.exercise.andrea.mediahandler.source.user;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.FIREBASE_PLAYLISTS_COLLECTION;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.FIREBASE_REALTIME_DATABASE;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.FIREBASE_USERS_COLLECTION;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.FIREBASE_VIDEOS_COLLECTION;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_TOPICS_OF_INTEREST;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unimib.exercise.andrea.mediahandler.models.User;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * Class that gets the user information using Firebase Realtime Database.
 */
public class UserDataRemoteDataSource extends BaseUserDataRemoteDataSource {

    private static final String TAG = UserDataRemoteDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public UserDataRemoteDataSource(SharedPreferencesUtil sharedPreferencesUtil) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    @Override
    public void saveUserData(User user) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User already present in Firebase Realtime Database");
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                } else {
                    Log.d(TAG, "User not present in Firebase Realtime Database");
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                userResponseCallback.onSuccessFromRemoteDatabase(user);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    @Override
    public void getUserPlaylists(String idToken) {
        Log.d(TAG, "getUserPlaylists: ");
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
            child(FIREBASE_PLAYLISTS_COLLECTION).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Error getting data", task.getException());
                    userResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                }
                else {
                    List<Playlist> playlistList = new ArrayList<>();
                    //Log.d(TAG, "Successful read: " + task.getResult().getValue());
                    for(DataSnapshot ds : task.getResult().getChildren()) {
                        Playlist playlist = ds.getValue(Playlist.class);
                        playlistList.add(playlist);
                    }
                    userResponseCallback.onSuccessFromPlaylistListSync(playlistList,idToken );
                }
            });

    }

    @Override
    public void getUserVideos(String idToken) {
        Log.d(TAG, "getUserVideos: ");
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_VIDEOS_COLLECTION).get().addOnCompleteListener(task -> {
                    List<Video> videoList = new ArrayList<>();
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting data", task.getException());
                        userResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                    }
                    else {
                        //Log.d(TAG, "Successful read: " + task.getResult().getValue());
                        for(DataSnapshot ds : task.getResult().getChildren()) {
                            Video video = ds.getValue(Video.class);
                            videoList.add(video);
                        }
                        userResponseCallback.onSuccessFromVideoListSync(videoList);
                    }
                });
    }

    @Override
    public void getUserPreferences(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
            child(SHARED_PREFERENCES_COUNTRY_OF_INTEREST).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String countryOfInterest = task.getResult().getValue(String.class);
                    sharedPreferencesUtil.writeStringData(
                            SHARED_PREFERENCES_FILE_NAME,
                            SHARED_PREFERENCES_COUNTRY_OF_INTEREST,
                            countryOfInterest);

                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                        child(SHARED_PREFERENCES_TOPICS_OF_INTEREST).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<String> favoriteTopics = new ArrayList<>();
                                    for(DataSnapshot ds : task.getResult().getChildren()) {
                                        String favoriteTopic = ds.getValue(String.class);
                                        favoriteTopics.add(favoriteTopic);
                                    }

                                    if (favoriteTopics.size() > 0) {
                                        Set<String> favoriteNewsSet = new HashSet<>(favoriteTopics);
                                        favoriteNewsSet.addAll(favoriteTopics);

                                        sharedPreferencesUtil.writeStringSetData(
                                                SHARED_PREFERENCES_FILE_NAME,
                                                SHARED_PREFERENCES_TOPICS_OF_INTEREST,
                                                favoriteNewsSet);
                                    }
                                    userResponseCallback.onSuccessFromGettingUserPreferences();
                                }
                            }
                        });
                }
            });
    }

    @Override
    public void saveUserPlaylists(List<Playlist> playlistList, String idToken) {
        //TODO Add listeners to manage error cases
        Log.d(TAG, "saveUserPlaylists: idToken:" + idToken +  ",  playlist:"+ playlistList );
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_PLAYLISTS_COLLECTION).setValue(playlistList);
        

        /*databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_TOPICS_OF_INTEREST).setValue(new ArrayList<>(playlistList));*/
    }

    @Override
    public void saveUserVideos(List<Video> videoList, String idToken) {
        Log.d(TAG, "saveUserVideos: idToken:" + idToken +  ",  videoList:"+ videoList);
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_VIDEOS_COLLECTION).setValue(videoList);
    }
}
