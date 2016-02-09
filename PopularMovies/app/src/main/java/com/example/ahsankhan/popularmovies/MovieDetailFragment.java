package com.example.ahsankhan.popularmovies;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link Fragment} subclass to show movie details.
 */
public class MovieDetailFragment extends Fragment {

    static final String MOVIE_ID = "MOVIE_ID";
    private static final String LOG_TAG =
            MovieDetailFragment.class.getSimpleName();
    TextView movieTitle = null;
    ImageView moviePoster = null;
    TextView movieYear = null;
    TextView movieRating = null;
    TextView movieSynopsis = null;
    String movieId = null;
    CardView trailerCardView = null;
    Boolean isInFavorite = false;
    String movieName;
    MovieTrailerAdapter trailerAdapter = null;
    ArrayAdapter<String> reviewAdapter = null;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,
                container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            movieId = arguments.getString(MovieDetailFragment.MOVIE_ID);
            Log.v(LOG_TAG, "Got movie Id" + movieId);
        } else {
            Log.v(LOG_TAG, "NO arguments");
        }

        movieTitle = (TextView) rootView.findViewById(R.id.detail_movie_title);
        moviePoster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        movieYear = (TextView) rootView.findViewById(R.id.detail_movie_year);
        movieRating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
        movieSynopsis = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
        trailerCardView = (CardView) rootView.findViewById(R.id.video_card);

        // Movie trailers
        trailerAdapter = new MovieTrailerAdapter(getActivity(),
                new ArrayList<MovieTile.MovieTrailer>());
        ListView trailerList = (ListView) rootView.findViewById(R.id.trailers_listview);
        trailerList.setAdapter(this.trailerAdapter);

        // Reviews
        reviewAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.review_item, R.id.review_text);
        ListView reviewList = (ListView) rootView.findViewById(R.id.review_listview);
        reviewList.setAdapter(reviewAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_favorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = isInFavorite ?
                        "Removed from favorite" : "Added to favorite";
                if (isInFavorite) {
                    TheMovieDBUtility.removeFromFavorite(getContext(), movieId);
                } else {
                    TheMovieDBUtility.addToFavorite(getContext(), movieId, movieName);
                }
                isInFavorite = TheMovieDBUtility.isFavoriteMovie(getContext(), movieId);
                Snackbar.make(view, status, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        updateDetail();
        return rootView;
    }


    void updateDetail() {
        new FetchMovieDetailTask().execute(movieId);
    }

    public static class MovieTrailerAdapter extends ArrayAdapter<MovieTile.MovieTrailer> {
        private static final String LOG_TAG =
                MovieTrailerAdapter.class.getSimpleName();

        public MovieTrailerAdapter(Activity context, ArrayList<MovieTile.MovieTrailer> trailers) {
            super(context, 0, trailers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Gets the AndroidFlavor object from the ArrayAdapter at
            // the appropriate position
            final MovieTile.MovieTrailer trailer = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.movie_trailer_item, parent, false);
            }
            if (convertView == null) return null;
            TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_text);
            ImageButton playTrailerButton = (ImageButton) convertView.findViewById(
                    R.id.play_button);
            trailerText.setText(trailer.trailerName);
            playTrailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // From:stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                    String trailerKey = trailer.trailerKey;
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("vnd.youtube:" + trailerKey));
                        getContext().startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
                        getContext().startActivity(intent);
                    }
                }
            });
            return convertView;
        }

    }

    private class FetchMovieDetailTask extends AsyncTask<String, Void, MovieTile> {
        @Override
        protected MovieTile doInBackground(String... params) {
            return TheMovieDBUtility.getDetail(params[0]);
        }

        @Override
        protected void onPostExecute(MovieTile movie) {
            if (movie == null) return;
            MovieDetailFragment.this.movieTitle.setText(movie.title);
            Picasso.with(getActivity()).load(movie.posterPath).into(
                    MovieDetailFragment.this.moviePoster);
            MovieDetailFragment.this.movieYear.setText(movie.releaseYear);
            MovieDetailFragment.this.movieRating.setText(movie.movieRating);
            MovieDetailFragment.this.movieSynopsis.setText(movie.movieSummary);
            if (movie.movieTrailers != null) {
                Log.v(LOG_TAG, "Aded trailers " + movie.movieTrailers.size());
            } else {
                Log.v(LOG_TAG, "No trailers");
            }
            trailerAdapter.clear();
            trailerAdapter.addAll(movie.movieTrailers);
            reviewAdapter.clear();
            reviewAdapter.addAll(movie.reviews);

            movieName = movie.title;
            isInFavorite = TheMovieDBUtility.isFavoriteMovie(getContext(), movie.id);
        }
    }
}
