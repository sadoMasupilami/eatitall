package at.fhjoanneum.airkoality.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        httpsGetTask.execute("https://api.openaq.org/v1/locations?country=AT");
    }

    @Override
    public void onRequestStart() {
        progressDialog.setMessage("Wird geladen...");
        progressDialog.show();
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
        runOnUiThread(() -> locationListFragment.update(locations));
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

            Location location = new Location(locationName, city, country);
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
