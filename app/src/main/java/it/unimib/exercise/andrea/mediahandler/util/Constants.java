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
    public static final String YOUTUBE_API_BASE_URL = "“https://www.googleapis.com/youtube/v3";
    public static final String TOP_SEARCH_ENDPOINT = "/search/";
    public static final String TOP_PLAYLIST_ENDPOINT = "/playlists";

    public static final String TOP_HEADLINES_COUNTRY_PARAMETER = "country";
    public static final String TOP_HEADLINES_PAGE_SIZE_PARAMETER = "pageSize";
    public static final int TOP_HEADLINES_PAGE_SIZE_VALUE = 100;

    // Constants for refresh rate of news
    public static final String LAST_UPDATE = "last_update";
    public static final int FRESH_TIMEOUT = 60*60*1000; // 1 hour in milliseconds

    // Constants for Room database
    public static final String NEWS_DATABASE_NAME = "news_db";
    public static final int DATABASE_VERSION = 1;
    public static final String YOUTUBE_DATABASE_NAME = "youtube_db";

    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
}
