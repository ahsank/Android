package com.example.ahsankhan.sunshine;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
		.add(R.id.container, new PlaceholderFragment())
		.commit();
        }
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

    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,
				 ViewGroup container,
				 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_main,
					     container, false);
	    String [] forecasts = {
		"Today Sunday 88/63",
		"Tomorrow Monday 88/22",
		"Tuesday 55/22",
		"Wednesday 99/55"
	    };
	    ArrayList<String> items = new ArrayList<String>(Arrays.asList(forecasts));
	    ArrayAdapter<String> adapter =
		new ArrayAdapter<String>(getActivity(), // parent activity
					 R.layout.list_item_laout,
					 R.id.list_item_forecast_textview,
					 items);
	    ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
	    listView.setAdapter(adapter);
            return rootView;
        }
    }
}
