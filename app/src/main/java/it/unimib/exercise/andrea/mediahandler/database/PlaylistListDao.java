package it.unimib.exercise.andrea.mediahandler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface PlaylistListDao {
    @Query("SELECT * FROM playlist ORDER BY id DESC")
    List<Playlist> getAllPlaylists();

    @Query("SELECT * FROM playlist WHERE id = :id")
    Playlist getPlaylist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylistList(List<Playlist> playlistList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylist(Playlist playlist);

    @Insert
    void insertAll(Playlist... playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("DELETE FROM playlist")
    void deleteAll();

    @Delete
    void deleteAllWithoutQuery(Playlist... playlist);

    @Update
    int updateSingleFavoritePlaylist(Playlist playlist);

    @Update
    int updateListFavoritePlaylist(List<Playlist> playlist);

    @Query("SELECT * FROM video Where playlistId = :playlistId ORDER BY position ASC")
    List<Video> getVideoListFromPlaylistId(String playlistId);

    @Query("SELECT last_update FROM playlist Where id = :playlistId")
    Long getLastUpdateFromPlaylist(String playlistId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVideoList(List<Video> videoList);

    @Query("SELECT * FROM video Where id_video_in_playlist = :videoIdInPlaylist")
    Video getVideo(String videoIdInPlaylist);

    @Update
    void updateVideo(Video video);
}
