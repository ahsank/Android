package com.example.ahsankhan.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ahsankhan on 12/9/15.
 * Besed on Udacity Sunshine App
 */
public class Utility {
    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
            context);
        return prefs.getString(context.getString(R.string.pref_sort_by_key),
                               context.getString(R.string.pref_sort_by_default));
    }
}
