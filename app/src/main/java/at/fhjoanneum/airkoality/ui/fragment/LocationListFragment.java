package at.fhjoanneum.airkoality.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.db.room.AirKoalityDB;
import at.fhjoanneum.airkoality.db.room.LocationDAO;
import at.fhjoanneum.airkoality.model.Location;
import at.fhjoanneum.airkoality.ui.adapter.LocationListAdapter;

public class LocationListFragment extends Fragment implements LocationListAdapter.LocationItemClickListener {

    private RecyclerView rvLocations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        rvLocations = view.findViewById(R.id.rvLocations);
        rvLocations.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void update(List<Location> locations) {
        LocationListAdapter locationListAdapter = new LocationListAdapter(locations, this);
        rvLocations.setAdapter(locationListAdapter);
    }

    @Override
    public void onLocationItemClicked(Location location) {
        Toast.makeText(getContext(), location.getLocation(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LIFECYCLE", "onPause location");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("LIFECYCLE", "onStop location");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LIFECYCLE", "onDestroy location");

    }
}
