package com.zenos.thunderstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CityWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> weatherDescriptions = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("weatherDescription");
        Log.v("HashMapTest", weatherDescriptions.toString());
    }
}
