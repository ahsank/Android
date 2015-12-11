package com.example.ahsankhan.popularmovies;

/**
 * Created by ahsankhan on 12/5/15.
 */
public class MovieTile {
    String id;
    String title;
    String posterPath;

    public MovieTile(String id, String title, String posterPath) {
	    this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

   // Following properties are only avaoid in movie detail response.
    String releaseYear;
    String movieRating;
    String movieSummary;

    public void setMovieDetails(String releaseYear, String rating,
                           String summary) {
        this.releaseYear = releaseYear;
        this.movieRating = rating;
        this.movieSummary = summary;
    }
}
