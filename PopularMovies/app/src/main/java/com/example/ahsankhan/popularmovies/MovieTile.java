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
}
