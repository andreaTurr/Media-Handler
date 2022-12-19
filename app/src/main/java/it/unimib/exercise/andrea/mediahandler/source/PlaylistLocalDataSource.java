package it.unimib.exercise.andrea.mediahandler.source;


import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * Class to get news from local database using Room.
 */
public class PlaylistLocalDataSource extends BasePlaylistLocalDataSource {

    private final PlaylistDao playlistDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public PlaylistLocalDataSource(YoutubeRoomDatabase youtubeRoomDatabase,
                                   SharedPreferencesUtil sharedPreferencesUtil) {
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
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() ->{
            playlistCallback.onSuccessFromLocal(playlistDao.getAll());
        });
    }
}
