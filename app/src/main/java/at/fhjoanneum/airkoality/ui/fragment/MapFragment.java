package at.fhjoanneum.airkoality.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.model.Location;
import at.fhjoanneum.airkoality.ui.MeasurementActivity;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mapView;
    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.i("LIFECYCLE", "onPause map");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        Log.i("LIFECYCLE", "onStop map");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.i("LIFECYCLE", "onDestroy map");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(true);
    }

    public void addMapMarkers(List<Location> locations) {
        if(map != null) {
            map.clear();
            for (Location location : locations) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(location.getLocation())
                        .snippet(location.getCity() + ", " + location.getCountry())
                        .position(new LatLng(location.getLatitude(), location.getLongitude()));
                map.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getContext(), MeasurementActivity.class);
        intent.putExtra("location_name", marker.getTitle());
        startActivity(intent);
    }
}
