package com.example.ahsankhan.myappportfolio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onSpotifyStreamerClicked(View view) {
        showToast("Spotify Streamer Clicked!");
    }

    public void onFootballScoresClicked(View view) {
        showToast("Football Scores App Clicked!");
    }

    public void onLibraryClicked(View view) {
        showToast("Library App Clicked!");
    }

    public void onBuildBiggerClicked(View view) {
        showToast("Build it Bigger Clicked!");
    }

    public void onXYZReaderClicked(View view) {
        showToast("XYZ Reader Clicked!");
    }

    public void onCapstoneClicked(View view) {
        showToast("Capstone My Own App Clicked!");
    }
}
