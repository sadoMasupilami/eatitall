package at.fhjoanneum.airkoality.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnvMain);
        bottomNavigationView.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.action_locations:
                    switchToFragment(FRAGMENT_LOCATIONS);
                    break;
                case R.id.action_map:
                    switchToFragment(FRAGMENT_MAP);
                    break;
                default:
                    break;
            }
            return true;
        });

        locationListFragment = new LocationListFragment();
        mapFragment = new MapFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.flFragmentContainer, locationListFragment);
        transaction.add(R.id.flFragmentContainer, mapFragment);
        transaction.hide(locationListFragment);
        transaction.hide(mapFragment);
        transaction.commit();
        switchToFragment(FRAGMENT_LOCATIONS);

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
