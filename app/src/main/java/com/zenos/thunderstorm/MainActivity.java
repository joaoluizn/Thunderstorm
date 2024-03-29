package com.zenos.thunderstorm;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.zenos.thunderstorm.utils.Connectivity;
import com.zenos.thunderstorm.utils.Inloco;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng currLocation;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Inloco.initInlocoSDK(MainActivity.this,
                getResources().getString(R.string.inloco_sdk_key), true);
        Inloco.initInlocoRequestPermission(MainActivity.this);
        View mainView = findViewById(R.id.mainView);
        checkConnectivity(mainView);
        handleFloatingButton();
        initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "Map fragment didn't load properly");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //TODO: Get Current Position enhance usability
        currLocation = new LatLng(-8.05, -34.9);
        MarkerOptions initMarker = new MarkerOptions().position(currLocation).title("Press Search to Seek Weather Here");

        //  Initial market on map
        mMap.addMarker(initMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));

        //  Map update handler
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Press Search to Seek Weather Here"));
                currLocation = latLng;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private void handleFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                requestNearLocations(view, currLocation);
                String snackString = String.format(Locale.ENGLISH,
                        "Searching Selected Location: (%.2f, %.2f)",
                        currLocation.latitude, currLocation.longitude);
                createDefaultSnackbar(view, snackString);
            }
        });
    }

    public void requestNearLocations(final View view, LatLng coordinate) {
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        /*
         * NOTE: API params used:
         * - lat, lon: latitude and longitude from desired point
         * - cnt: Number max of returned cities (The returned quantity could be less than this.
         * */
        String openWeatherUrl = String.format(Locale.ENGLISH,
                getResources().getString(R.string.open_weather_api_url),
                coordinate.latitude, coordinate.longitude,
                getResources().getString(R.string.open_weather_key));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, openWeatherUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                      #TODO: temporary SnackBar -> this will be and intent to cities list activity
                        Intent intent = new Intent(MainActivity.this, NearPlacesActivity.class);
                        intent.putExtra("json", response.toString());
                        startActivity(intent);

                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (checkConnectivity(view)) {
                            createDefaultSnackbar(view,
                                    "No places found. Pick a new position.");
                            Log.d(TAG, "Error on request" + error.getMessage());
                        }
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void createDefaultSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    private boolean checkConnectivity(View view) {
        if (!Connectivity.isOnline()) {
            String snackString = String.format(Locale.ENGLISH,
                    "Phone is not connected, please check connection.");
            createDefaultSnackbar(view, snackString);
            return false;
        }
        return true;
    }

    //


}
