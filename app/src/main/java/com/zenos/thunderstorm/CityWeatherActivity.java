package com.zenos.thunderstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CityWeatherActivity extends AppCompatActivity {

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
        minTemp.setText(intent.getStringExtra("minTemp") + "ºC");
        maxTemp.setText(intent.getStringExtra("maxTemp") + "ºC");
        humidity.setText(intent.getStringExtra("humidity") + "%");
        windSpeed.setText(intent.getStringExtra("windSpeed") + " m/s");
        ArrayList<HashMap<String, String>> weatherDescriptions = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("weatherDescription");

        weather.setText(weatherDescriptions.get(0).get("type"));
        Picasso.get().load(weatherDescriptions.get(0).get("iconUrl")).into(weatherIcon);
    }
}
