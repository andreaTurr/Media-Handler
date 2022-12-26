package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.util.Log;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * Class to get news from local database using Room.
 */
public class PlaylistLocalDataSource extends BasePlaylistLocalDataSource {
    private static final String TAG = PlaylistLocalDataSource.class.getSimpleName();
    private final PlaylistDao playlistDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public PlaylistLocalDataSource(YoutubeRoomDatabase youtubeRoomDatabase, SharedPreferencesUtil sharedPreferencesUtil) {
        this.playlistDao = youtubeRoomDatabase.playlistDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    /**
     * Gets the playlist from the local database.
     * The method is executed with an ExecutorService defined in YoutubeRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getPlaylist() {
        Log.d(TAG, "getPlaylist: ");
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() ->{
            playlistCallback.onSuccessFromLocal(playlistDao.getAll());
        });
    }

    @Override
    public void insertPlaylists(PlaylistApiResponse playlistApiResponse) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {

            List<Playlist> playlistList = playlistApiResponse.getPlaylistList();
            playlistDao.insertPlaylistList(playlistList);
            Log.d(TAG, "insertPlaylists: " + playlistList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            playlistCallback.onSuccessFromLocal(playlistList);
        });
    }


}
