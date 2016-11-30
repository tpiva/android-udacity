package com.example.android.sunshine.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tmagalhaes on 30-Nov-16.
 */

public class WeatherDataParser {

    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(weatherJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        if(dayIndex < jsonArray.length()) {
            JSONObject object = jsonArray.getJSONObject(dayIndex).getJSONObject("temp");
            return object.getDouble("max");
        }

        return -1;
    }

}
