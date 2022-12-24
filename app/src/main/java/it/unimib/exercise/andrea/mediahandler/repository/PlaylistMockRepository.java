package it.unimib.exercise.andrea.mediahandler.repository;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.PLAYLIST_API_TEST_JSON_FILE;

import android.app.Application;

import java.io.IOException;
import java.util.List;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.database.PlaylistDao;
import it.unimib.exercise.andrea.mediahandler.database.YoutubeRoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlist.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlist.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.util.JSONParserUtil;

public class PlaylistMockRepository implements IPlaylistRepository{
    private final Application application;
    private final ResponseCallback responseCallback;
    private final PlaylistDao playlistDao;

    public PlaylistMockRepository(Application application, ResponseCallback responseCallback, PlaylistDao playlistDao) {
        this.application = application;
        this.responseCallback = responseCallback;
        this.playlistDao = playlistDao;
    }


    @Override
    public void fetchPlaylist() {
        PlaylistApiResponse playlistApiResponse = null;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        try {
            playlistApiResponse = jsonParserUtil.parseJSONFileWithGSon(PLAYLIST_API_TEST_JSON_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (playlistApiResponse != null) {
            saveDataInDatabase(playlistApiResponse.getPlaylistList());
        } else {
            responseCallback.onFailure(application.getString(R.string.error_retrieving_playlists));
        }
    }

    @Override
    public void updatePlaylist(Playlist news) {

    }

    /**
     * Saves the news in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param playlistList the list of news to be written in the local database.
     */
    private void saveDataInDatabase(List<Playlist> playlistList) {
        /*YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Playlist> allNews = playlistDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Playlist playlist : allNews) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (playlistList.contains(playlist)) {
                    // The primary key is contained only in the Playlist ,objects retrieved from the
                    // database, and not in the Playlist objects downloaded from the Web Service.
                    // If the same playlist was already downloaded earlier, the following
                    // line of code replaces the the Playlist object in playlistList with the
                    // corresponding Playlist object saved in the database, so that it has the
                    // primary key.
                    playlistList.set(playlistList.indexOf(playlist), playlist);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            playlistDao.insertPlaylistList(playlistList);
            for (int i = 0; i < playlistList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                playlistList.get(i).setId(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(playlistList, System.currentTimeMillis());
        });*/
    }

    /**
     * Gets the playlist from the local database.
     * The method is executed with an ExecutorService defined in YoutubeRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate) {
        YoutubeRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(playlistDao.getAll(), lastUpdate);
        });
    }

}
