package it.unimib.exercise.andrea.mediahandler.util;

import android.app.Application;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.repository.IPlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.repository.PlaylistRepositoryWithLiveData;
import it.unimib.exercise.andrea.mediahandler.service.YoutubeApiService;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.BasePlaylistRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistLocalDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistMockRemoteDataSource;
import it.unimib.exercise.andrea.mediahandler.source.PlaylistRemoteDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

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
    public YoutubeApiService getNewsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.YOUTUBE_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(YoutubeApiService.class);
    }

    /**
     * Returns an instance of YoutubeRoomDatabase class to manage Room database.
     * @param application Param for accessing the global application state.
     * @return An instance of YoutubeRoomDatabase.
     */
    public YoutubeRoomDatabase getNewsDao(Application application) {
        return YoutubeRoomDatabase.getDatabase(application);
    }

    /**
     * Returns an instance of IPlaylistRepositoryWithLiveData.
     * @param application Param for accessing the global application state.
     * @param debugMode Param to establish if the application is run in debug mode.
     * @return An instance of IPlaylistRepositoryWithLiveData.
     */
    public IPlaylistRepositoryWithLiveData getNewsRepository(Application application, boolean debugMode) {
        BasePlaylistRemoteDataSource playlistRemoteDataSource;
        BasePlaylistLocalDataSource playlistLocalDataSource;
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        if (debugMode) {
            JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
            playlistRemoteDataSource =
                    new PlaylistMockRemoteDataSource(jsonParserUtil);
        } else {
            playlistRemoteDataSource =
                    new PlaylistRemoteDataSource(application.getString(R.string.WEBSERVER_API_KEY_VALUE));
        }

        playlistLocalDataSource = new PlaylistLocalDataSource(getNewsDao(application), sharedPreferencesUtil);

        return new PlaylistRepositoryWithLiveData(playlistRemoteDataSource, playlistLocalDataSource);
    }
}
