package at.fhjoanneum.airkoality.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import at.fhjoanneum.airkoality.AirKoalityApplication;
import at.fhjoanneum.airkoality.R;
import at.fhjoanneum.airkoality.ui.MainActivity;

public class LocationService extends Service {
    Notification notification;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LocationService", "Service created");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification = new NotificationCompat.Builder(this, AirKoalityApplication.CHANNEL_ID)
                .setContentTitle("AirKoality Location Service")
                .setContentText("Updating location")
                .setSmallIcon(R.drawable.ic_location_searching_black_24dp)
                .setContentIntent(pendingIntent).build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLocations().size() != 0) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationService.this);
                    preferences.edit().putFloat("latitude", (float) locationResult.getLocations().get(0).getLatitude()).putFloat("longitude", (float) locationResult.getLocations().get(0).getLongitude()).apply();

                    Log.d("LocationService", "Location: " + locationResult.getLocations().get(0).getLatitude()
                    + " - " + locationResult.getLocations().get(0).getLongitude());
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationService", "Service started");
        startForeground(123, notification);
        startLocationUpdates();
        return START_STICKY;
    }

    private void startLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        Log.d("LocationService", "Service destroyed");
    }
}
