package com.example.ahsankhan.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG =
            DiscoverMovieTask.class.getSimpleName();

    private MovieTileAdapter movieTilesAdapter = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,
                                         container, false);
        this.movieTilesAdapter = new MovieTileAdapter(getActivity(),
                new ArrayList<MovieTile>());

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(this.movieTilesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {
                MovieTile movieTile = movieTilesAdapter.getItem(position);

                Intent detailIntent = new Intent(getActivity(),
                        MovieDetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieTile.id);
                startActivity(detailIntent);
            }
        });

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            updateView();
        } else {
            ArrayList<MovieTile> list =
                    savedInstanceState.getParcelableArrayList("key");
            movieTilesAdapter.addAll(list);
        }


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movieTilesAdapter != null) {
            ArrayList<MovieTile> items = new ArrayList<>();
            for (int i = 0; i < movieTilesAdapter.getCount(); i++) {
                items.add(movieTilesAdapter.getItem(i));
            }
            outState.putParcelableArrayList("key", items);
        }
        super.onSaveInstanceState(outState);
    }

    public void updateView() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String sort_by =
                preferences.getString(getString(R.string.pref_sort_by_key),
                        getString(R.string.pref_sort_by_default));

        new DiscoverMovieTask().execute(sort_by);
    }

    public static class MovieTileAdapter extends ArrayAdapter<MovieTile> {
        private static final String LOG_TAG =
            MovieTileAdapter.class.getSimpleName();

        public MovieTileAdapter(Activity context, List<MovieTile> movieTiles) {
            super(context, 0, movieTiles);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Gets the AndroidFlavor object from the ArrayAdapter at
            // the appropriate position
            MovieTile movie = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.movies_grid_item, parent, false);
            }
            if (convertView == null) return null;

            ImageView imageView =
		(ImageView) convertView.findViewById(R.id.movie_image);
            if (imageView != null) {
                // Todo: Ahsan: Add placeholder and error image.
                Picasso.with(getContext())
                        .load(movie.posterPath)
                        .into(imageView);
            }

            TextView movieTitleView =
		(TextView) convertView.findViewById(R.id.movie_text);
            if (movieTitleView != null) {
                movieTitleView.setText(movie.title);
            }
            return convertView;
        }
    }

    private class DiscoverMovieTask extends AsyncTask<String, Void, MovieTile[]> {
        @Override
        protected MovieTile[] doInBackground(String...params) {
            String sort_option = params[0];
            if (sort_option.equals("favorite")) {
                return TheMovieDBUtility.getFavoriteMovies(getContext());
            } else {
                return TheMovieDBUtility.discover(sort_option);
            }
        }
        @Override
        protected void onPostExecute(MovieTile[] result) {
            if (result == null) return;
            MainActivityFragment.this.movieTilesAdapter.clear();
            MainActivityFragment.this.movieTilesAdapter.addAll(result);
        }
    }
}
