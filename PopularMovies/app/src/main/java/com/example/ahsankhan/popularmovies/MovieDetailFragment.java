package com.example.ahsankhan.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
// import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    TextView movieTitle = null;
    ImageView moviePoster = null;
    TextView movieYear = null;
    TextView movieRating = null;
    TextView movieSynopsis = null;
    String movieId = null;

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
        moviePoster=
            (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        movieYear = (TextView) rootView.findViewById(R.id.detail_movie_year);
        movieRating =
            (TextView) rootView.findViewById(R.id.detail_movie_rating);
        movieSynopsis =
            (TextView) rootView.findViewById(R.id.detail_movie_synopsis);;
	this.movieId =
            getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        updateDetail();
        return rootView;
    }

    void updateDetail() {
        new FetchMovieTask() {
            @Override
            protected void onPostExecute(MovieTile[] result) {
                if (result == null || result.length == 0) return;
                MovieTile movie = result[0];
                MovieDetailFragment.this.movieTitle.setText(movie.title);
                Picasso.with(getActivity()).load(movie.posterPath).into(
                    MovieDetailFragment.this.moviePoster);
                MovieDetailFragment.this.movieYear.setText(movie.releaseYear);
                MovieDetailFragment.this.movieRating.setText(movie.movieRating);
                MovieDetailFragment.this.movieSynopsis.setText(movie.movieSummary);
            }
        }.execute("detail", movieId);
    }

}
