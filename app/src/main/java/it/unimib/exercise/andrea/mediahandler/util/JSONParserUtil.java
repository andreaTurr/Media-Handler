package it.unimib.exercise.andrea.mediahandler.util;


import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.unimib.exercise.andrea.mediahandler.models.playlists.PlaylistApiResponse;

/**
 * Utility class to show different ways to parse a JSON file.
 */
public class JSONParserUtil {

    private static final String TAG = JSONParserUtil.class.getSimpleName();
    private final Application application;

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    /**
     * Returns a list of News from a JSON file parsed using Gson.
     * Doc can be read here: https://github.com/google/gson
     * @param fileName The JSON file to be parsed.
     * @return The NewsApiResponse object associated with the JSON file content.
     * @throws IOException
     */
    public PlaylistApiResponse parseJSONFileWithGSon(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, PlaylistApiResponse.class);
    }
}

