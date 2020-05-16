package at.fhjoanneum.airkoality.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.db.room.AirKoalityDB;
import at.fhjoanneum.airkoality.model.Location;
import at.fhjoanneum.airkoality.network.HttpsGetTask;
import at.fhjoanneum.airkoality.network.RequestCallback;
import at.fhjoanneum.airkoality.service.LocationService;
import at.fhjoanneum.airkoality.ui.adapter.LocationListAdapter;
import at.fhjoanneum.airkoality.ui.fragment.LocationListFragment;
import at.fhjoanneum.airkoality.ui.fragment.MapFragment;

public class MainActivity extends AppCompatActivity implements RequestCallback {

    MapFragment mapFragment;
    LocationListFragment locationListFragment;

    private static final String FRAGMENT_LOCATIONS = "FragmentLocations";
    private static final String FRAGMENT_MAP = "FragmentMap";

    private static final String PREF_KEY_FRAGMENT = "fragment";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnvMain);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        bottomNavigationView.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.action_locations:
                    prefs.edit().putString(PREF_KEY_FRAGMENT, FRAGMENT_LOCATIONS).apply();
                    switchToFragment(FRAGMENT_LOCATIONS);
                    break;
                case R.id.action_map:
                    prefs.edit().putString(PREF_KEY_FRAGMENT, FRAGMENT_MAP).apply();
                    switchToFragment(FRAGMENT_MAP);
                    break;
                default:
                    break;
            }
            return true;
        });

        progressDialog = new ProgressDialog(this);

        locationListFragment = new LocationListFragment();
        mapFragment = new MapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, locationListFragment);
        transaction.add(R.id.flFragmentContainer, mapFragment);
        transaction.hide(locationListFragment);
        transaction.hide(mapFragment);
        transaction.commit();

        String fragmentName = prefs.getString(PREF_KEY_FRAGMENT, FRAGMENT_LOCATIONS);

        switchToFragment(fragmentName);

        fetchLocations();
    }

    private void fetchLocations() {
        HttpsGetTask httpsGetTask = new HttpsGetTask(this);

        String request;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        float latitude = preferences.getFloat("latitude", -1000);
        float longitude = preferences.getFloat("longitude", -1000);

        if (latitude == -1000 || longitude == -1000) {
            request = "https://api.openaq.org/v1/locations?country=AT";
        } else {
            request = String.format("https://api.openaq.org/v1/locations?coordinates=%f,%f&radius=200000&limit=10000", latitude, longitude);
        }
        httpsGetTask.execute(request);
    }

    @Override
    public void onRequestStart() {
        progressDialog.setMessage("Wird geladen...");
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_service:
                startLocationService();
                return true;
            case R.id.action_stop_service:
                stopLocationService();
                return true;
            case R.id.action_refresh:
                fetchLocations();
                return true;
            default:
                return false;
        }
    }

    private void startLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            }
        }
    }

    private void stopLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    @Override
    public void onResult(String result) {
        new Thread(() -> {
            List<Location> locations = null;

            if (result == null) {
                Log.e("HTTPrequest", "Error");
                locations = AirKoalityDB.getInstance(this).locationDAO().getAll();
            } else {
                try {
                    locations = parseLocations(result);
                    AirKoalityDB.getInstance(this).locationDAO().addAll(locations);
                } catch (JSONException e) {
                    e.getStackTrace();
                }
            }

            runOnUiThread(() -> {
                progressDialog.dismiss();
            });
            updateFragments(locations);
        }).start();
    }

    private void updateFragments(List<Location> locations) {
        runOnUiThread(() -> {
            locationListFragment.update(locations);
            mapFragment.addMapMarkers(locations);
        });
    }

    private List<Location> parseLocations(String json) throws JSONException {
        List<Location> locations = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject locationResult = results.getJSONObject(i);
            String locationName = locationResult.getString("location");
            String city = locationResult.getString("city");
            String country = locationResult.getString("country");

            JSONObject coordinates = locationResult.getJSONObject("coordinates");
            double latitude = coordinates.getDouble("latitude");
            double longitude = coordinates.getDouble("longitude");

            Location location = new Location(locationName, city, country, latitude, longitude);
            locations.add(location);
        }
        return locations;
    }


    private void switchToFragment(String fragmentName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentName) {
            case FRAGMENT_LOCATIONS:
                transaction.hide(mapFragment);
                transaction.show(locationListFragment);
                break;
            case FRAGMENT_MAP:
                transaction.hide(locationListFragment);
                transaction.show(mapFragment);
                break;
            default:
                break;
        }

        transaction.commit();
    }

}
