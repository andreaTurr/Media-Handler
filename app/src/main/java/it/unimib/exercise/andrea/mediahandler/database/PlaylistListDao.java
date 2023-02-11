package it.unimib.exercise.andrea.mediahandler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.VideoPartial;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistPartial;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface PlaylistListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNewPlaylistList(List<Playlist> playlistList);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNewVideoList(List<Video> videoList);

    @Query("SELECT * FROM playlist ORDER BY id DESC")
    List<Playlist> getAllPlaylists();
    @Query("SELECT * FROM playlist WHERE id = :id")
    Playlist getPlaylist(String id);
    @Query("SELECT last_update FROM playlist Where id = :playlistId")
    Long getLastUpdateFromPlaylist(String playlistId);
    @Query("SELECT * FROM video Where playlistId = :playlistId ORDER BY position ASC")
    List<Video> getVideoListFromPlaylistId(String playlistId);
    @Query("SELECT * FROM video Where id_video_in_playlist = :videoIdInPlaylist")
    Video getVideo(String videoIdInPlaylist);

    @Update
    void updateVideo(Video video);
    @Update(entity = Video.class)
    void updateVideoPartial(VideoPartial video);
    @Update
    void updatePlaylist(Playlist playlist);
    @Update(entity = Playlist.class)
    void updatePlaylistPartial(PlaylistPartial playlistPartial);

    @Delete
    void deleteVideo(Video video);
    @Delete
    void delete(Playlist playlist);

    @Query("DELETE FROM Playlist")
    int deleteAllPlaylist();

    @Query("DELETE FROM Video")
    int deleteAllVideo();

}
