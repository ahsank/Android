package com.example.ahsankhan.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ahsankhan on 12/5/15.
 * Represens a movie information showin in the app.
 */
public class MovieTile implements Parcelable {
    String id;
    String title;
    String posterPath;

    private MovieTile(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        releaseYear = in.readString();
        movieRating = in.readString();
        movieSummary = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseYear);
        dest.writeString(movieRating);
        dest.writeString(movieSummary);
    }

    public static final Parcelable.Creator<MovieTile> CREATOR = new Parcelable.Creator<MovieTile>() {
        public MovieTile createFromParcel(Parcel in) {
            return new MovieTile(in);
        }

        public MovieTile[] newArray(int size) {
            return new MovieTile[size];
        }
    };
}
