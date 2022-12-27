package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_PLAYLIST_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.util.Log;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistListDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * Class to get news from local database using Room.
 */
public class PlaylistLocalDataSource extends BasePlaylistLocalDataSource {
    private static final String TAG = PlaylistLocalDataSource.class.getSimpleName();
    private final PlaylistListDao playlistListDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public PlaylistLocalDataSource(YoutubeRoomDatabase youtubeRoomDatabase, SharedPreferencesUtil sharedPreferencesUtil) {
        this.playlistListDao = youtubeRoomDatabase.playlistDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    /**
     * Gets the playlist from the local database.
     * The method is executed with an ExecutorService defined in YoutubeRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getPlaylistList() {
        Log.d(TAG, "getPlaylist: local");
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() ->{
            List<Playlist> list = playlistListDao.getAll();
            PlaylistApiResponse playlistApiResponse = new PlaylistApiResponse(list);
            //NewsApiResponse newsApiResponse = new NewsApiResponse();
            //newsApiResponse.setNewsList(newsDao.getAll());
            Log.d(TAG, "getPlaylist: " + list);
            playlistCallback.onSuccessFromLocalPlaylistList(playlistListDao.getAll());
        });
    }

    @Override
    public void insertPlaylistsList(PlaylistApiResponse playlistApiResponse) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {

            List<Playlist> playlistList = playlistApiResponse.getPlaylistList();
            playlistListDao.insertPlaylistList(playlistList);
            Log.d(TAG, "insertPlaylists: " + playlistList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE_PLAYLIST_LIST, String.valueOf(System.currentTimeMillis()));
            playlistCallback.onSuccessFromLocalPlaylistList(playlistList);
        });
    }

    @Override
    public void getPlaylist(String playlistId) {

    }

    @Override
    public void insertPlaylists(PlaylistApiResponse playlistList) {

    }


}
