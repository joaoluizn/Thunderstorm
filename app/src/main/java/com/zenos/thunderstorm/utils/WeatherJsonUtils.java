package com.zenos.thunderstorm.utils;

import android.content.res.Resources;

import com.zenos.thunderstorm.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WeatherJsonUtils {

    public static String getPlaceName(JSONArray placesList, int position){
        String name = "";
        try {
            name = placesList
                    .getJSONObject(position)
                    .getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getPlaceMinTemp(JSONArray placesList, int position){
        String minTemp = "";
        try {
            minTemp = placesList
                    .getJSONObject(position)
                    .getJSONObject("main")
                    .getString("temp_min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return minTemp;
    }

    public static String getPlaceMaxTemp(JSONArray placesList, int position){
        String maxTemp = "";
        try {
            maxTemp = placesList
                    .getJSONObject(position)
                    .getJSONObject("main")
                    .getString("temp_max");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maxTemp;
    }

    public static String getPlaceHumidity(JSONArray placesList, int position){
        String humidity = "";
        try {
            humidity = placesList
                    .getJSONObject(position)
                    .getJSONObject("main")
                    .getString("humidity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return humidity;
    }

    public static String getPlaceWindSpeed(JSONArray placesList, int position){
        String windSpeed = "";
        try {
            windSpeed = placesList
                    .getJSONObject(position)
                    .getJSONObject("wind")
                    .getString("speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return windSpeed;
    }



    public static ArrayList<HashMap<String, String>> getWeatherDescription(
            JSONArray placesList, int position, Resources rsc) {

        ArrayList<HashMap<String, String>> weatherDescriptions = new ArrayList<>();
        JSONArray weatherList = null;
        try {
            weatherList = placesList.getJSONObject(position).getJSONArray("weather");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < weatherList.length(); i++){
            HashMap<String, String> description = new HashMap<>();
            try {
                String iconUrl = String.format(Locale.ENGLISH,
                        rsc.getString(R.string.open_weather_icon_url),
                        weatherList.getJSONObject(i).getString("icon"));
                description.put("iconUrl", iconUrl);
                description.put("type", weatherList.getJSONObject(i).getString("description"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            weatherDescriptions.add(description);
        }

        return weatherDescriptions;
    }
}
