package it.unimib.exercise.andrea.mediahandler.database;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.DATABASE_VERSION;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.YOUTUBE_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

@Database(entities = {Playlist.class, Video.class, LocalVideo.class, LocalAudio.class},
        version = DATABASE_VERSION)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract PlaylistListDao playlistDao();

    public abstract LocalMediaDao localMediaDao();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, YOUTUBE_DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
