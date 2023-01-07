package it.unimib.exercise.andrea.mediahandler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;

@Dao
public interface LocalMediaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLocalVideo(LocalVideo localVideos);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLocalAudio(LocalAudio localAudio);

    @Query("SELECT * FROM localvideo WHERE name = :name")
    LocalVideo getLocalVideo(String name);
    @Query("SELECT * FROM localvideo")
    List<LocalVideo> getLocalVideo();
    @Query("SELECT * FROM localaudio WHERE name = :name")
    LocalAudio getLocalAudio(String name);
    @Query("SELECT * FROM localaudio")
    List<LocalAudio> getLocalAudio();


    @Update
    void updateLocalVideo(LocalVideo video);
    @Update
    void updateLocalAudio(LocalAudio audio);

    @Delete
    void deleteLocalVideo(LocalVideo video);
    @Delete
    void deleteLocalAudio(LocalAudio audio);
}
