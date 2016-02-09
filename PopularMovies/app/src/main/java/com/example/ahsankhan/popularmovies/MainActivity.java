package com.example.ahsankhan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mSortBy;
    private boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortBy = Utility.getPreferredSortOrder(this);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
	    Intent settingIntent =
                new Intent(this, SettingsActivity.class);
	    startActivity(settingIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = Utility.getPreferredSortOrder( this );
        if (sortBy == null || sortBy.equals(mSortBy)) return;

        // update the location in our second pane using the fragment manager
        MainActivityFragment maf = (MainActivityFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main);
        if ( null != maf ) maf.updateView();

        // DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        // if ( null != df ) df.onLocationChanged(location);

        mSortBy = sortBy;
    }

    public void onItemSelected(String movieId) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(MovieDetailFragment.MOVIE_ID, movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,
                            fragment, DETAILFRAGMENT_TAG)
                    .commit();
            Log.v("ActivityMain", "Started detail fragment");

        } else {
            Intent detailIntent = new Intent(this,
                    MovieDetailActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, movieId);
            startActivity(detailIntent);
            Log.v("ActivityMain", "Started detail intent");


        }
    }
}
