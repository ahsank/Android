package com.example.ahsankhan.sunshine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ahsankhan on 11/24/15.
 */
public class WeatherDataParser {
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
	throws JSONException {
	JSONObject jObject = new JSONObject(weatherJsonStr);
	return jObject
	    .getJSONArray("list")
	    .getJSONObject(dayIndex)
	    .getJSONObject("temp")
	    .getDouble("max");
    }
}
