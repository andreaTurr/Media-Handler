package it.unimib.exercise.andrea.mediahandler.database;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.DATABASE_VERSION;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.YOUTUBE_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

@Database(entities = {Playlist.class, Video.class}, version = DATABASE_VERSION)
public abstract class YoutubeRoomDatabase extends RoomDatabase {

    public abstract PlaylistListDao playlistDao();

    private static volatile YoutubeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static YoutubeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (YoutubeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            YoutubeRoomDatabase.class, YOUTUBE_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
