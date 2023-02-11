package it.unimib.exercise.andrea.mediahandler.source.playlist;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.LAST_UPDATE_PLAYLIST_LIST;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.LOCAL_SOURCE_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.util.Log;

import java.util.List;

import it.unimib.exercise.andrea.mediahandler.database.PlaylistListDao;
import it.unimib.exercise.andrea.mediahandler.database.RoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.PlaylistItemApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.Video;
import it.unimib.exercise.andrea.mediahandler.models.playlistItem.VideoPartial;
import it.unimib.exercise.andrea.mediahandler.models.playlists.Playlist;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;
import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistPartial;
import it.unimib.exercise.andrea.mediahandler.util.SharedPreferencesUtil;

/**
 * Class to get news from local database using Room.
 */
public class PlaylistLocalDataSource extends BasePlaylistLocalDataSource {
    private static final String TAG = PlaylistLocalDataSource.class.getSimpleName();
    private final PlaylistListDao playlistListDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public PlaylistLocalDataSource(RoomDatabase roomDatabase, SharedPreferencesUtil sharedPreferencesUtil) {
        this.playlistListDao = roomDatabase.playlistDao();
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
        RoomDatabase.databaseWriteExecutor.execute(() ->{
            List<Playlist> list = playlistListDao.getAllPlaylists();
            Log.d(TAG, "getPlaylist: " + list);

            if (!list.isEmpty()){
                PlaylistApiResponse playlistApiResponse = new PlaylistApiResponse(list);
                PlaylistApiResponse playlistItemApiResponse =
                        new PlaylistApiResponse(list);
                playlistCallback.onSuccessFromLocalPlaylistList(playlistItemApiResponse);
            }else{
                sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                        LAST_UPDATE_PLAYLIST_LIST, "0");
                playlistCallback.onFailureFromRemotePlaylistList(
                        new Exception(LOCAL_SOURCE_ERROR));
            }

        });
    }
//todo update method to handle a response containing multiple api's calls content
    /**
     *
     * @param playlistApiResponse this response should contain the list of ALL playlist of the
     *                            channel, so it could contain multiple api's calls content
     *
     */
    @Override
    public void insertPlaylistsList(PlaylistApiResponse playlistApiResponse) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            //todo handle playlist deleted by user and not in user account anymore
            List<Playlist> newPlaylistList = playlistApiResponse.getPlaylistList();
            List<Playlist> oldPlaylistList = playlistListDao.getAllPlaylists();

            //delete not present anymore
            for (Playlist playlist: oldPlaylistList) {
                if (!newPlaylistList.contains(playlist)){
                    playlistListDao.delete(playlist);
                    Log.d(TAG, "insertPlaylistsList: deleted " + playlist);
                }
            }
            oldPlaylistList = playlistListDao.getAllPlaylists();
            //update already present
            for (Playlist playlist: newPlaylistList) {
                if (oldPlaylistList.contains(playlist)){
                    playlistListDao.updatePlaylistPartial(new PlaylistPartial(playlist));
                    Log.d(TAG, "insertPlaylistsList: updated " + playlist);
                }
            }

            playlistListDao.insertNewPlaylistList(newPlaylistList);
            Log.d(TAG, "insertPlaylists: " + newPlaylistList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE_PLAYLIST_LIST, String.valueOf(System.currentTimeMillis()));

            //update response with playlists saved
            newPlaylistList = playlistListDao.getAllPlaylists();
            playlistApiResponse.setPlaylistList(newPlaylistList);
            playlistCallback.onSuccessFromLocalPlaylistList(playlistApiResponse);
        });
    }



    @Override
    public void getPlaylistLastUpdate(String playlistId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Long lastUpdate = playlistListDao.getLastUpdateFromPlaylist(playlistId);
            playlistCallback.onSuccessFromLocalLastUpdate(lastUpdate, playlistId);
        });
    }

    @Override
    public void getVideoList(String playlistId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getVideoList: Local");
            List<Video> list = playlistListDao.getVideoListFromPlaylistId(playlistId);
            PlaylistItemApiResponse playlistItemApiResponse = new PlaylistItemApiResponse(list);
            Log.d(TAG, "getVideoList: " + list);
            if (!list.isEmpty()) //check if database empty
                playlistCallback.onSuccessFromLocalVideoList(playlistItemApiResponse);
            else
                playlistCallback.onFailureFromLocalVideoList(new Exception(LOCAL_SOURCE_ERROR));
        });
    }

    @Override
    public void insertVideoList(PlaylistItemApiResponse playlistItemApiResponse, String playlistId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "insertVideoList: ");
            List<Video> newVideoList = playlistItemApiResponse.getVideoList();
            List<Video> oldVideoList = playlistListDao.getVideoListFromPlaylistId(playlistId);

            //delete video not present anymore
            for (Video video:
                 oldVideoList) {
                if(!newVideoList.contains(video)){
                    playlistListDao.deleteVideo(video);
                    Log.d(TAG, "insertVideoList: deleted " + video);
                }
            }
            //update already present video
            oldVideoList = playlistListDao.getVideoListFromPlaylistId(playlistId);
            for (Video video:
                    newVideoList) {
                if(oldVideoList.contains(video)){
                    playlistListDao.updateVideoPartial(new VideoPartial(video));
                    Log.d(TAG, "insertVideoList: updated " + video);
                }
            }

            playlistListDao.insertNewVideoList(newVideoList);
            //update lastUpdate
            Playlist playlist = playlistListDao.getPlaylist(playlistId);
            playlist.setLastUpdate(System.currentTimeMillis());
            playlistListDao.updatePlaylist(playlist);

            //get video in playlist, taken from local to have correct values
            newVideoList = playlistListDao.getVideoListFromPlaylistId(playlistId);
            playlistItemApiResponse.setVideoList(newVideoList);
            //callback
            playlistCallback.onSuccessFromLocalVideoList(playlistItemApiResponse);
        });
    }

    @Override
    public void insertVideo(Video video) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "insertVideo: " + video);
            playlistListDao.updateVideo(video);
            List<Video> list = playlistListDao.getVideoListFromPlaylistId(video.getSnippet().getPlaylistId());
            PlaylistItemApiResponse playlistItemApiResponse = new PlaylistItemApiResponse(list);
            playlistCallback.onSuccessFromLocalVideoList(playlistItemApiResponse);
        });
    }

    @Override
    public void getVideo(String videoIdInPlaylist) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getVideo: Local");
            Video video = playlistListDao.getVideo(videoIdInPlaylist);
            if (video != null)
                playlistCallback.onSuccessFromLocalVideo(video);
            else
                playlistCallback.onFailureFromLocalVideo(new Exception(LOCAL_SOURCE_ERROR));
        });
    }

    @Override
    public void deleteAll() {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "deleteAll");
            int newsCounter = playlistListDao.getAllPlaylists().size();
            int newsDeletedNews = playlistListDao.deleteAllPlaylist();
            playlistListDao.deleteAllVideo();

            Log.d(TAG, "newsCounter: " + newsCounter);
            Log.d(TAG, "newsDeletedNews: " + newsDeletedNews);
            // It means that everything has been deleted
            if (newsCounter == newsDeletedNews) {
                sharedPreferencesUtil.deleteAll(SHARED_PREFERENCES_FILE_NAME);
                playlistCallback.onSuccessDeletion();
            }

        });
    }

}
