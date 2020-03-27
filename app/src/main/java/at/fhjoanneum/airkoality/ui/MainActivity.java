package at.fhjoanneum.airkoality.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.model.Location;
import at.fhjoanneum.airkoality.ui.adapter.LocationListAdapter;
import at.fhjoanneum.airkoality.ui.fragment.LocationListFragment;
import at.fhjoanneum.airkoality.ui.fragment.MapFragment;

public class MainActivity extends AppCompatActivity {

    Fragment mapFragment;
    Fragment locationListFragment;

    private static final String FRAGMENT_LOCATIONS = "FragmentLocations";
    private static final String FRAGMENT_MAP = "FragmentMap";

    private static final String PREF_KEY_FRAGMENT = "fragment";


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

        locationListFragment = new LocationListFragment();
        mapFragment = new MapFragment();

        String fragmentName = prefs.getString(PREF_KEY_FRAGMENT, FRAGMENT_LOCATIONS);

        switchToFragment(fragmentName);

    }

    private void switchToFragment(String fragmentName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentName) {
            case FRAGMENT_LOCATIONS:
                transaction.replace(R.id.flFragmentContainer, locationListFragment);
                break;
            case FRAGMENT_MAP:
                transaction.replace(R.id.flFragmentContainer, mapFragment);
                break;
            default:
                break;
        }

        transaction.commit();
    }

}
