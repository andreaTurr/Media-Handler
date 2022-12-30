package it.unimib.exercise.andrea.mediahandler.source;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_PLAYLIST_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.util.Log;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistListDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
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
            List<Playlist> list = playlistListDao.getAllPlaylists();
            PlaylistApiResponse playlistApiResponse = new PlaylistApiResponse(list);
            //NewsApiResponse newsApiResponse = new NewsApiResponse();
            //newsApiResponse.setNewsList(newsDao.getAll());
            Log.d(TAG, "getPlaylist: " + list);
            PlaylistApiResponse playlistItemApiResponse =
                    new PlaylistApiResponse(list);
            playlistCallback.onSuccessFromLocalPlaylistList(playlistItemApiResponse);
        });
    }

    @Override
    public void insertPlaylistsList(PlaylistApiResponse playlistApiResponse) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {

            //todo handle playlist deleted by user and not in user account anymore
            List<Playlist> playlistList = playlistApiResponse.getPlaylistList();
            playlistListDao.insertPlaylistList(playlistList);
            Log.d(TAG, "insertPlaylists: " + playlistList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE_PLAYLIST_LIST, String.valueOf(System.currentTimeMillis()));
            playlistCallback.onSuccessFromLocalPlaylistList(playlistApiResponse);
        });
    }

    @Override
    public void getPlaylistLastUpdate(String playlistId) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long lastUpdate = playlistListDao.getLastUpdateFromPlaylist(playlistId);
            playlistCallback.onSuccessFromLocalLastUpdate(lastUpdate, playlistId);
        });
    }

    @Override
    public void getVideoList(String playlistId) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getVideoList: Local");
            List<Video> list = playlistListDao.getVideoFromPlaylist(playlistId);
            PlaylistItemApiResponse playlistItemApiResponse = new PlaylistItemApiResponse(list);
            Log.d(TAG, "getVideoList: " + list);
            if (list != null)
                playlistCallback.onSuccessFromLocalVideoList(playlistItemApiResponse);
            else
                playlistCallback.onFailureFromLocalVideoList(new Exception("test"));
        });
    }

    @Override
    public void insertVideoList(PlaylistItemApiResponse playlistItemApiResponse, String playlistId) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "insertVideoList: ");
            List<Video> videoList = playlistItemApiResponse.getVideoList();
            playlistListDao.insertVideoList(videoList);
            //update last update
            Playlist playlist = playlistListDao.getPlaylist(playlistId);
            playlist.setLastUpdate(System.currentTimeMillis());
            playlistListDao.insertPlaylist(playlist);
            //callback
            playlistCallback.onSuccessFromLocalVideoList(playlistItemApiResponse);
        });
    }
}
