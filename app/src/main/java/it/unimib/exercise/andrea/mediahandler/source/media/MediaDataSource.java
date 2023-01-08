package it.unimib.exercise.andrea.mediahandler.source.media;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.database.LocalMediaDao;
import it.unimib.exercise.andrea.mediahandler.database.RoomDatabase;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.LocalAudio;
import it.unimib.exercise.andrea.mediahandler.models.localAudio.ResultLocalAudios;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.LocalVideo;
import it.unimib.exercise.andrea.mediahandler.models.localVideo.ResultLocalVideos;

public class MediaDataSource extends BaseMediaDataSource {
    private static final String TAG = MediaDataSource.class.getSimpleName();
    private final LocalMediaDao localMediaDao;
    private final Context context;

    public MediaDataSource(RoomDatabase roomDatabase, Context context) {
        this.localMediaDao = roomDatabase.localMediaDao();
        this.context = context;
    }

    @Override
    public void getVideos() {
        Log.d(TAG, "getVideos: ");
        List<LocalVideo> localVideos = new ArrayList<>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA
        };
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES))};
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int pathColum = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.

                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                String videoPath = cursor.getString(pathColum);
                Log.d(TAG, "getVideos: name=" + name);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                Bitmap thumbnail = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {  //29
                    thumbnail =
                            context.getContentResolver().loadThumbnail(
                                    contentUri, new Size(640, 480), null);
                }else{
                    thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                }
                // Stores column values and the contentUri in a local object
                // that represents the media file.

                localVideos.add(new LocalVideo(contentUri, name, 0, duration, size, thumbnail));
            }
            mediaCallback.onSuccessFromStorageVideo(new ResultLocalVideos.Success(localVideos));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInsertLocalVideo(LocalVideo localVideo) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getVideoLocalVideo: old=" + localVideo);
            localMediaDao.insertLocalVideo(localVideo);
            LocalVideo databaseInstance = localMediaDao.getLocalVideo(localVideo.getName());
            Log.d(TAG, "getVideoLocalVideo: new=" + databaseInstance);
            List<LocalVideo> list = new ArrayList<>();
            list.add(databaseInstance);
            mediaCallback.onSuccessFromGetVideoCurrentTime(new ResultLocalVideos.Success(list));
        });


    }

    @Override
    public void updateLocalVideo(LocalVideo localVideo) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            localMediaDao.updateLocalVideo(localVideo);
        });
    }

    @Override
    public void getAudios() {
        Log.d(TAG, "getAudios: ");
        List<LocalAudio> localAudios = new ArrayList<>();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
        };
        String selection = MediaStore.Audio.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES))};
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int authorColum = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int pathColum = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                String author = cursor.getString(authorColum);
                String path = cursor.getString(pathColum);

                Log.d(TAG, "getAudio: name=" + name);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                Bitmap thumbnail = null;
                try{
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {  //29
                        thumbnail =
                                context.getContentResolver().loadThumbnail(
                                        contentUri, new Size(640, 480), null);
                    }else{
                        thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
                    }
                }catch (IOException e){
                    Log.d(TAG, "getAudios: exception="+e.getMessage());
                }
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                localAudios.add(new LocalAudio(contentUri, name, 0L, duration, size, thumbnail, author));
            }
            mediaCallback.onSuccessFromStorageAudio(new ResultLocalAudios.Success(localAudios));
        } /*catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "getAudios: error=" + e.getMessage());
            Log.d(TAG, "getAudios: error=" + e.printStackTrace());
        }*/
    }

    @Override
    public void getInsertLocalAudio(LocalAudio localAudio) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getLocalAudio: old=" + localAudio);
            localMediaDao.insertLocalAudio(localAudio);
            LocalAudio databaseInstance = localMediaDao.getLocalAudio(localAudio.getName());
            Log.d(TAG, "getLocalAudio: new=" + databaseInstance);
            List<LocalAudio> list = new ArrayList<>();
            list.add(databaseInstance);
            mediaCallback.onSuccessFromGetAudioCurrentTime(new ResultLocalAudios.Success(list));
        });
    }

    @Override
    public void updateLocalAudio(LocalAudio localAudio) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            localMediaDao.updateLocalAudio(localAudio);
        });
    }
}
