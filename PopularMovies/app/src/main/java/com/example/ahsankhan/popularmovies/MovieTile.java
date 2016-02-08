package com.example.ahsankhan.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ahsankhan on 12/5/15.
 * Represens a movie information showin in the app.
 */
public class MovieTile implements Parcelable {
    public static final Parcelable.Creator<MovieTile> CREATOR
            = new Parcelable.Creator<MovieTile>() {
        public MovieTile createFromParcel(Parcel in) {
            return new MovieTile(in);
        }

        public MovieTile[] newArray(int size) {
            return new MovieTile[size];
        }
    };
    String id;
    String title;
    String posterPath;
    // Following properties are only available in movie detail response.
    String releaseYear;
    String movieRating;
    String movieSummary;
    ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
    ArrayList<String> reviews = new ArrayList<>();

    private MovieTile(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        releaseYear = in.readString();
        movieRating = in.readString();
        movieSummary = in.readString();
        in.readTypedList(movieTrailers, MovieTrailer.CREATOR);
        in.readStringList(reviews);
    }

    public MovieTile(String id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public void setMovieDetails(String releaseYear, String rating,
                                String summary) {
        this.releaseYear = releaseYear;
        this.movieRating = rating;
        this.movieSummary = summary;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        movieTrailers = trailers;
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
        dest.writeTypedList(movieTrailers);
        dest.writeStringList(reviews);
    }

    public static class MovieTrailer implements Parcelable {
        public static final Parcelable.Creator<MovieTrailer> CREATOR
                = new Parcelable.Creator<MovieTrailer>() {
            public MovieTrailer createFromParcel(Parcel in) {
                return new MovieTrailer(in);
            }

            public MovieTrailer[] newArray(int size) {
                return new MovieTrailer[size];
            }
        };
        String trailerName;
        String trailerKey;

        private MovieTrailer(Parcel in) {
            trailerName = in.readString();
            trailerKey = in.readString();
        }

        public MovieTrailer(String name, String key) {
            trailerName = name;
            trailerKey = key;
        }

        @Override
        public int describeContents() {
            return 1;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(trailerName);
            dest.writeString(trailerKey);
        }
    };
}
