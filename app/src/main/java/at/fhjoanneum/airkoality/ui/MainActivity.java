package at.fhjoanneum.airkoality.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.model.Location;
import at.fhjoanneum.airkoality.ui.adapter.LocationListAdapter;

public class MainActivity extends AppCompatActivity implements LocationListAdapter.LocationItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnvMain);

        RecyclerView rvLocations = findViewById(R.id.rvLocations);
        rvLocations.setLayoutManager(new LinearLayoutManager(this));

        List<Location> locations = new ArrayList<>();

        for(int i =0; i< 100; i++) {
            locations.add(new Location("Uhrturm " + i, "Graz", "Österreich"));
        }

        LocationListAdapter locationListAdapter = new LocationListAdapter(locations, this);
        rvLocations.setAdapter(locationListAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener((menuItem) -> {
            return true;
        });
    }

    @Override
    public void onLocationItemClicked(Location location) {
        Toast.makeText(this, location.getLocation(), Toast.LENGTH_SHORT).show();
    }
}
