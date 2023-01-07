package it.unimib.exercise.andrea.mediahandler.util;

import android.app.Application;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.database.RoomDatabase;
import it.unimib.exercise.andrea.mediahandler.repository.IMediaRepository;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.repository.MediaRepository;
import it.unimib.exercise.andrea.mediahandler.repository.PlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.service.YoutubeApiService;
import it.unimib.exercise.andrea.mediahandler.source.playlist.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.media.MediaDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.PlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.PlaylistMockRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.playlist.PlaylistRemoteDataSource;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {
    private AuthStateManager mStateManager = null;

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    /**
     * Returns an instance of ServiceLocator class.
     * @return An instance of ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Returns an instance of NewsApiService class using Retrofit.
     * @return an instance of NewsApiService.
     */
    public YoutubeApiService getYoutubeApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.YOUTUBE_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).client(client).build();
        return retrofit.create(YoutubeApiService.class);
    }

    /**
     * Returns an instance of RoomDatabase class to manage Room database.
     * @param application Param for accessing the global application state.
     * @return An instance of YoutubeRoomDatabase.
     */
    public RoomDatabase getRoomDatabase(Application application) {
        return RoomDatabase.getDatabase(application);
    }


    //menagement of AuthStateManager is inside the class itself
    public AuthStateManager getmStateManager(Application application) {
        return AuthStateManager.getInstance(application.getApplicationContext()) ;
    }

    /**
     * Returns an instance of IPlaylistRepositoryWithLiveData.
     * @param application Param for accessing the global application state.
     * @param debugMode Param to establish if the application is run in debug mode.
     * @return An instance of IPlaylistRepositoryWithLiveData.
     */
    public IPlaylistRepositoryWithLiveData getPlaylistRepository(Application application, boolean debugMode) {
        mStateManager = AuthStateManager.getInstance(application.getApplicationContext()) ;
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BasePlaylistRemoteDataSource playlistRemoteDataSource;
        BasePlaylistLocalDataSource playlistLocalDataSource;

        if (debugMode) {
            JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
            playlistRemoteDataSource =
                    new PlaylistMockRemoteDataSource(jsonParserUtil);
        } else {
            playlistRemoteDataSource =
                    new PlaylistRemoteDataSource( application.getApplicationContext(), mStateManager,
                            application.getString(R.string.YOUTUBE_API_KEY_VALUE));
        }

        playlistLocalDataSource = new PlaylistLocalDataSource(
                getRoomDatabase(application),
                sharedPreferencesUtil);
        return new PlaylistRepositoryWithLiveData(playlistRemoteDataSource, playlistLocalDataSource);
    }

    public IMediaRepository getMediaRepository(Application application){
        MediaDataSource mediaDataSource = new MediaDataSource(
                getRoomDatabase(application), application.getApplicationContext());
        return new MediaRepository(mediaDataSource);
    }

}
