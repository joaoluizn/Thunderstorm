package com.zenos.thunderstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CityWeatherActivity extends AppCompatActivity {

    private static final String TAG = "CityWeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        Intent intent = getIntent();
        TextView city = findViewById(R.id.city);
        TextView minTemp = findViewById(R.id.minTemp);
        TextView maxTemp = findViewById(R.id.maxTemp);
        TextView humidity = findViewById(R.id.humidity);
        TextView windSpeed = findViewById(R.id.wind);
        TextView weather = findViewById(R.id.weather);
        ImageView weatherIcon = findViewById(R.id.weatherIcon);

        city.setText(intent.getStringExtra("name"));
        minTemp.setText(String.format("%sºC", intent.getStringExtra("minTemp")));
        maxTemp.setText(String.format("%sºC", intent.getStringExtra("maxTemp")));
        humidity.setText(String.format("%s%%", intent.getStringExtra("humidity")));
        windSpeed.setText(String.format("%s m/s", intent.getStringExtra("windSpeed")));
        ArrayList<HashMap<String, String>> weatherDescriptions =
                (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("weatherDescription");

        String description = Objects.requireNonNull(weatherDescriptions).get(0).get("type");
        String capitalized = Objects.requireNonNull(description).substring(0, 1).toUpperCase() +
                description.substring(1);

        weather.setText(capitalized);
        Picasso.get().load(weatherDescriptions.get(0).get("iconUrl")).into(weatherIcon);
    }
}
