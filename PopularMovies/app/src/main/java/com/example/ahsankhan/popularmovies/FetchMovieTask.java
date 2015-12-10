package com.example.ahsankhan.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ahsankhan on 12/7/15.
 * Based on Udacity Sunshine App.
 */

public class FetchMovieTask extends AsyncTask<String, Void, MovieTile[]> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    final String MOVIEDB_URL = "http://api.themoviedb.org/3/";
    final String MOVIE_DISCOVER_BASE_URL = MOVIEDB_URL + "discover/movie?";

    final String API_KEY_PARAM = "api_key";
    final String SORT_PARAM="sort_by";

    @Override
    protected MovieTile[] doInBackground(String...params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        final String sort_by = params[0];

        try {
            // Construct the URL for the themoviedb query
            Uri builtUri = Uri.parse(MOVIE_DISCOVER_BASE_URL)
                .buildUpon()
                .appendQueryParameter(SORT_PARAM, sort_by)
                .appendQueryParameter(API_KEY_PARAM,
                                      BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
            String serverUrl = builtUri.toString();
            URL url = new URL(serverUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        
        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch(final JSONException e) {
            Log.e(LOG_TAG, "Error parsing json", e);
        }
        return null;
    }

    /*
      Json format:
       { "page":1, "results":[ { "adult":false,
        "backdrop_path":"/qjn3fzCAHGfl0CzeUlFbjrsmu4c.jpg",
        "genre_ids":[ 28, 12, 18 ], "id":131634,
        "original_language":"en", "original_title":"The Hunger Games:
        Mockingjay - Part 2", "overview":"With the nation of Panem in
        a full scale war....", "release_date":"2015-11-19",
        "poster_path":"/nN4cEJMHJHbJBsp3vvvhtNWLGqg.jpg",
        "popularity":27.924039, "title":"The Hunger Games: Mockingjay
        - Part 2", "video":false, "vote_average":7.2, "vote_count":524
        } ], "total_pages":12647, "total_results":252936 }

     */
    private MovieTile[] getMovieDataFromJson(String moviesJsonStr)
        throws JSONException {
        final String TITLE = "title";
        final String MOVIE_ID = "id";
        final String RESULTS = "results";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
	if (resultsArray.length() == 0) return null;
        MovieTile[] movieTiles = new MovieTile[resultsArray.length()];
        for (int i=0; i < resultsArray.length(); i++) {
            JSONObject movieJson = resultsArray.getJSONObject(i);
            movieTiles[i] = new MovieTile(movieJson.getString(MOVIE_ID),
                                          movieJson.getString(TITLE),
                                          getFullImagePath(movieJson));
        }
	return movieTiles;
    }

    private String getFullImagePath(JSONObject movieJson)
		throws JSONException {
		final String POSTER_PATH = "poster_path";
		return "http://image.tmdb.org/t/p/w185/" +
            movieJson.getString(POSTER_PATH);
    }
}
