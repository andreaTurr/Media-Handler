package it.unimib.exercise.andrea.mediahandler.util;

/**
 * Utility class to save constants used by the app.
 */
public class Constants {

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "it.unimib.worldnews.exercise.andrea.preference";
    public static final String SHARED_PREFERENCES_COUNTRY_OF_INTEREST = "country_of_interest";
    public static final String SHARED_PREFERENCES_TOPICS_OF_INTEREST = "topics_of_interest";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "it.unimib.worldnews.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";

    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "it.unimib.worldnews.encrypted_file.txt";

    // Constants for files contained in assets folder
    public static final String PLAYLIST_API_TEST_JSON_FILE = "playlist.json";

    // Constants for YOUTUBE_API https://www.googleapis.com/youtube/v3
    public static final String YOUTUBE_API_BASE_URL = "https://youtube.googleapis.com/youtube/v3/";
    public static final String TOP_SEARCH_ENDPOINT = "search/";
    public static final String PLAYLIST_LIST_ENDPOINT = "playlists?part=snippet%2CcontentDetails&maxResults=50&mine=true";
    public static final String PLAYLIST_ITEMS_ENDPOINT = "playlistItems?part=snippet%2CcontentDetails&maxResults=50";
    public static final String VIDEO_DETAILED_ENDPOINT = "videos?part=contentDetails&maxResults=50";


//playlistItems?part=snippet%2CcontentDetails&playlistId=PLsizmByY7_uV0J7feYX4pta_05SV5SkJr&key
    public static final String TOP_HEADLINES_COUNTRY_PARAMETER = "country";
    public static final String TOP_HEADLINES_PAGE_SIZE_PARAMETER = "pageSize";
    public static final int TOP_HEADLINES_PAGE_SIZE_VALUE = 100;

    // Constants for refresh rate of news
    public static final String LAST_UPDATE_PLAYLIST_LIST = "last_update";
    public static final String LAST_UPDATE_VIDEO_LIST = "last_update";
    public static final int FRESH_TIMEOUT = 60*60*1000; // 1 hour in milliseconds

    // Constants for Room database
    public static final String NEWS_DATABASE_NAME = "news_db";
    public static final int DATABASE_VERSION = 16;
    public static final String YOUTUBE_DATABASE_NAME = "youtube_db";

    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String LOCAL_SOURCE_ERROR = "local_source_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    public static final int SECONDS_IN_HOUR = 60 * 60;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int DIVIDER_INSET = 16 ;

    // Constants for Firebase Realtime Database
    public static final String FIREBASE_REALTIME_DATABASE = "https://myuniproject-371314-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_PLAYLISTS_COLLECTION = "saved_playlists";
    public static final String FIREBASE_VIDEOS_COLLECTION = "saved_videos";
    public static final int MINIMUM_PASSWORD_LENGTH = 6;
}
