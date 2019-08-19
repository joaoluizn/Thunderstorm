package com.zenos.thunderstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getPlaceHumidity;
import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getPlaceMaxTemp;
import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getPlaceMinTemp;
import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getPlaceName;
import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getPlaceWindSpeed;
import static com.zenos.thunderstorm.utils.WeatherJsonUtils.getWeatherDescription;

public class NearPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_places);
        buildListView();
    }

    private void buildListView(){
        ListView placesListView = findViewById(R.id.places_list);

        if (getIntent().hasExtra("json")) {
            JSONArray placesFoundArray = getPlacesJsonArray();
            List<String> placesNames = getPlacesNames(placesFoundArray);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    placesNames);

            placesListView.setAdapter(arrayAdapter);
            createListViewListener(placesListView, placesFoundArray);
        }
    }

    private JSONArray getPlacesJsonArray() {
        try {
            return new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("json")))
                    .getJSONArray("list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private List<String> getPlacesNames(JSONArray placesList) {
        List<String> placesNames = new ArrayList<>();
        for (int i = 0; i < placesList.length(); i++) {
            try {
                placesNames.add(placesList.getJSONObject(i).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesNames;
    }

    private void createListViewListener(ListView lv, final JSONArray placesList){
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NearPlacesActivity.this, CityWeatherActivity.class);
//                Weather description
                String name = getPlaceName(placesList, position);
                String minTemp = getPlaceMinTemp(placesList, position);
                String maxTemp = getPlaceMaxTemp(placesList, position);
                String humidity = getPlaceHumidity(placesList, position);
                String windSpeed = getPlaceWindSpeed(placesList, position);

                ArrayList<HashMap<String, String>> weatherDescription = getWeatherDescription(
                        placesList, position, getResources());

                Log.d("NearPlacesActivity", "Name: " + name);
                Log.d("NearPlacesActivity", "MinTemp:" + minTemp);
                Log.d("NearPlacesActivity", "MaxTemp:" + maxTemp);
                Log.d("NearPlacesActivity", "Humidity:" + humidity);
                Log.d("NearPlacesActivity", "WindSpeed:" + windSpeed);
                Log.d("NearPlacesActivity", "WDescription:" + weatherDescription.toString());

                intent.putExtra("name", name);
                intent.putExtra("minTemp", minTemp);
                intent.putExtra("maxTemp", maxTemp);
                intent.putExtra("humidity", humidity);
                intent.putExtra("windSpeed", windSpeed);
                intent.putExtra("weatherDescription", weatherDescription);

                startActivity(intent);
            }
        });
    }


}
