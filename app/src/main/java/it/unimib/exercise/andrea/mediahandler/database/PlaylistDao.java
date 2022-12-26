package it.unimib.exercise.andrea.mediahandler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlists.*;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM playlist ORDER BY id DESC")
    List<Playlist> getAll();

    @Query("SELECT * FROM playlist WHERE id = :id")
    Playlist getPlaylist(long id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylistList(List<Playlist> playlistList);

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
}
