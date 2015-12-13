package com.example.ahsankhan.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahsankhan on 12/13/15.
 * Based on Udacity Sunshine App.
 */
public class TheMovieDBUtility {
    static private final String LOG_TAG = TheMovieDBUtility.class.getSimpleName();
    static final String MOVIEDB_URL = "http://api.themoviedb.org/3/";
    static final String MOVIE_DISCOVER_BASE_URL = MOVIEDB_URL + "discover/movie?";
    static final String MOVIE_DETAIL_BASE_URL = MOVIEDB_URL + "movie/";

    static final String API_KEY_PARAM = "api_key";
    static final String SORT_PARAM="sort_by";

    static MovieTile[] discover(String sortBy) {
        String movieJsonStr = NetworkUtility.FetchJson(
            MOVIE_DISCOVER_BASE_URL,
            new String[]{SORT_PARAM, API_KEY_PARAM},
            new String[]{sortBy, BuildConfig.THE_MOVIE_DB_API_KEY});
        try {
            return getMovieListFromJson(movieJsonStr);
        } catch(final JSONException e) {
            Log.e(LOG_TAG, "Error parsing json", e);
        }
        return null;
    }
    
    static MovieTile getDetail(String movieId) {
        String movieJsonStr = NetworkUtility.FetchJson(
            MOVIE_DETAIL_BASE_URL + movieId + "?",
            new String[] {API_KEY_PARAM},
            new String[] {BuildConfig.THE_MOVIE_DB_API_KEY});
        try {
            return getMovieDetailFromJson(movieJsonStr);
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
    static private MovieTile[] getMovieListFromJson(String moviesJsonStr)
        throws JSONException {
        final String TITLE = "title";
        final String MOVIE_ID = "id";
        final String RESULTS = "results";
        
        if (moviesJsonStr == null) return null;
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

    static private String getFullImagePath(JSONObject movieJson)
		throws JSONException {
        final String POSTER_PATH = "poster_path";
        return "http://image.tmdb.org/t/p/w185/" +
            movieJson.getString(POSTER_PATH);
    }

    /*
      Movie detail Json format:

      { "adult":false,
      "backdrop_path":"/qjn3fzCAHGfl0CzeUlFbjrsmu4c.jpg",
   "belongs_to_collection":{ "id":131635, "name":"The Huger Games
   Collection", "poster_path":"/cEBNDEMGqvSvU0knEv9Wl3dk5kv.jpg",
   "backdrop_path":"/lWZB4VFSlU292oJ3ZAzAzPi9GU0.jpg" },
   "budget":125000000,
    "genres":[ { "id":18, "name":"Drama" }, {
   "id":12, "name":"Adventure" }, { "id":28, "name":"Action" } ],
   "homepage":"http://www.thehungergames.movie/", "id":131634,
   "imdb_id":"tt1951266", "original_language":"en",
   "original_title":"The Hunger Games: Mockingjay - Part 2",
   "overview":"With the nation of Panem in a full scale war, Katniss
   confronts .....", "popularity":23.800512,
   "poster_path":"/nN4cEJMHJHbJBsp3vvvhtNWLGqg.jpg",
   "production_companies":[ { "name":"Studio Babelsberg", "id":264 },
   { "name":"Lionsgate", "id":1632 }, { "name":"Color Force",
   "id":5420 } ], 
   "production_countries":[ { "iso_3166_1":"US",
   "name":"United States of America" } ],
    "release_date":"2015-11-19",
   "revenue":101025000, "runtime":136, 
   "spoken_languages":[ {
   "iso_639_1":"en", "name":"English" } ]
,   "status":"Released",
   "tagline":"The fire will burn forever.", "title":"The Hunger Games:
   Mockingjay - Part 2", "video":false, "vote_average":7.1,
   "vote_count":556 }

     */
   static private MovieTile getMovieDetailFromJson(String moviesJsonStr)
        throws JSONException {
       if (moviesJsonStr == null) return null;
       JSONObject movieJson = new JSONObject(moviesJsonStr);
       MovieTile movieTile = new MovieTile(movieJson.getString("id"),
                                           movieJson.getString("title"),
                                           getFullImagePath(movieJson));
       movieTile.setMovieDetails(getYear(movieJson.getString("release_date")),
                                 movieJson.getString("vote_average") + "/10",
                                 movieJson.getString("overview"));
       return movieTile;
    }

   static private String getYear(String date) {
       SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
       Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(date));
            return Integer.toString(cal.get(Calendar.YEAR));
        } catch (ParseException e) {
            Log.v(LOG_TAG, "Error parsing date " + date, e);
        }
        return "";
   }
}
