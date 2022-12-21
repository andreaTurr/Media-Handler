package it.unimib.exercise.andrea.mediahandler.source;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;

/**
 * Class to get news from local database using Room.
 */
public class PlaylistLocalDataSource extends BasePlaylistLocalDataSource {

    private final PlaylistDao playlistDao;

    public PlaylistLocalDataSource(YoutubeRoomDatabase youtubeRoomDatabase) {
        this.playlistDao = youtubeRoomDatabase.playlistDao();
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

    @Override
    public void insertPlaylists(List<Playlist> playlistList) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Playlist> allPlaylists = playlistDao.getAll();
            if (!playlistList.equals(allPlaylists)) {
                playlistDao.insertPlaylistList(playlistList);
                playlistCallback.onSuccessFromLocal(playlistList);
            }
        });
    }


}
