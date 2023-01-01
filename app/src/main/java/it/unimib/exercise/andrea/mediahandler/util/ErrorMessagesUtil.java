package it.unimib.exercise.andrea.mediahandler.util;

import static it.unimib.exercise.andrea.mediahandler.util.Constants.API_KEY_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.LOCAL_SOURCE_ERROR;
import static it.unimib.exercise.andrea.mediahandler.util.Constants.RETROFIT_ERROR;

import android.app.Application;

import it.unimib.exercise.andrea.mediahandler.R;

/**
 * Utility class to get the proper message to be show
 * to the user when an error occurs.
 */
public class ErrorMessagesUtil {

    private Application application;

    public ErrorMessagesUtil(Application application) {
        this.application = application;
    }

    /**
     * Returns a message to inform the user about the error.
     * @param errorType The type of error.
     * @return The message to be shown to the user.
     */
    public String getErrorMessage(String errorType) {
        switch(errorType) {
            case RETROFIT_ERROR:
                return application.getString(R.string.error_retrieving_playlists);
            case API_KEY_ERROR:
                return application.getString(R.string.api_key_error);
            case LOCAL_SOURCE_ERROR:
                return application.getString(R.string.local_source_error);
            default:
                return application.getString(R.string.unexpected_error);
        }
    }
}
