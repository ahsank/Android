package com.example.ahsankhan.popularmovies;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import com.squareup.picasso.Picasso;


/**
 * A {@link Fragment} subclass to show movie details.
 */
public class MovieDetailFragment extends Fragment {

    TextView movieTitle = null;
    ImageView moviePoster = null;
    TextView movieYear = null;
    TextView movieRating = null;
    TextView movieSynopsis = null;
    String movieId = null;
    CardView trailerCardView = null;
    TextView trailerText = null;
    String trailerKey = null;
    ImageButton playTrailerButton = null;
    Boolean isInFavorite = false;
    String movieName;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail,
                container, false);
        movieTitle = (TextView) rootView.findViewById(R.id.detail_movie_title);
        moviePoster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        movieYear = (TextView) rootView.findViewById(R.id.detail_movie_year);
        movieRating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
        movieSynopsis = (TextView) rootView.findViewById(R.id.detail_movie_synopsis);
        movieId = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        trailerCardView = (CardView) rootView.findViewById(R.id.video_card);
        trailerText = (TextView) rootView.findViewById(R.id.trailer_text);
        playTrailerButton = (ImageButton) rootView.findViewById(R.id.play_button);
        playTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From:stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube:" + trailerKey));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
                    startActivity(intent);
                }
            }
        });
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
            if (movie.movieTrailers != null && movie.movieTrailers.size() > 0) {
                MovieTile.MovieTrailer trailer = movie.movieTrailers.get(0);
                trailerText.setText(trailer.trailerName);
                trailerKey = trailer.trailerKey;
            }
            movieName = movie.title;
            isInFavorite = TheMovieDBUtility.isFavoriteMovie(getContext(), movie.id);
        }
    }
}
