package at.fhjoanneum.eatitall.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.db.room.AirKoalityDB;
import at.fhjoanneum.eatitall.model.LatestMeasurements;
import at.fhjoanneum.eatitall.model.Measurement;
import at.fhjoanneum.eatitall.network.HttpsGetTask;
import at.fhjoanneum.eatitall.network.RequestCallback;
import at.fhjoanneum.eatitall.util.Util;

public class MeasurementActivity extends AppCompatActivity implements RequestCallback {

    private AirKoalityDB database;

    private String locationName;

    private TextView tvLocation;
    private TextView tvBCvalue;
    private TextView tvCOvalue;
    private TextView tvO3value;
    private TextView tvPM10value;
    private TextView tvPM25value;
    private TextView tvSO2value;
    private TextView tvNO2value;

    private NetworkStateReceiver networkStateReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        tvLocation = findViewById(R.id.tvLocation);
        tvBCvalue = findViewById(R.id.tvBCvalue);
        tvCOvalue = findViewById(R.id.tvCOvalue);
        tvO3value = findViewById(R.id.tvO3value);
        tvPM10value = findViewById(R.id.tvPM10value);
        tvPM25value = findViewById(R.id.tvPM25value);
        tvSO2value = findViewById(R.id.tvSO2value);
        tvNO2value = findViewById(R.id.tvNO2value);

        locationName = getIntent().getStringExtra("location_name");

        tvLocation.setText(locationName);
        database = AirKoalityDB.getInstance(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(R.string.latest_measurements);

        networkStateReceiver = new NetworkStateReceiver();

        fetchLatestMeasurements(locationName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.measurement_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            try {
                View rootView = findViewById(R.id.rootView);
                Bitmap bitmap = Util.getBitmapFromView(rootView);
                Uri uri = Util.savePng(this, bitmap, "measurements.png");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, "Measurements from " + locationName);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Airkoality Measurements");
                intent.setType("image/png");

                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void fetchLatestMeasurements(String locationName) {
        HttpsGetTask httpsGetTask = new HttpsGetTask(this);
        httpsGetTask.execute("https://api.openaq.org/v1/latest?location=" + locationName);
    }

    @Override
    public void onRequestStart() {
    }

    @Override
    public void onResult(String result) {
        new Thread(() -> {
            LatestMeasurements latestMeasurements;

            if (result == null) {
                latestMeasurements = database.latestMeasurementDAO().getForLocation(locationName);
            } else {
                latestMeasurements = parseLatestMeasurements(result);
                if (latestMeasurements != null) {
                    database.latestMeasurementDAO().add(latestMeasurements);
                }
            }

            updateUI(latestMeasurements);

        }).start();
    }

    public void updateUI(LatestMeasurements latestMeasurements) {
        if (latestMeasurements != null) {
            runOnUiThread(() -> {
                for (Measurement measurement : latestMeasurements.getMeasurements()) {
                    String valueString = "" + measurement.getValue() + " " + measurement.getUnit();
                    switch (measurement.getParameter().toLowerCase()) {
                        case "bc":
                            tvBCvalue.setText(valueString);
                            break;
                        case "co":
                            tvCOvalue.setText(valueString);
                            break;
                        case "no2":
                            tvNO2value.setText(valueString);
                            break;
                        case "o3":
                            tvO3value.setText(valueString);
                            break;
                        case "pm10":
                            tvPM10value.setText(valueString);
                            break;
                        case "pm25":
                            tvPM25value.setText(valueString);
                            break;
                        case "so2":
                            tvSO2value.setText(valueString);
                            break;
                    }
                }
            });
        }
    }


    private LatestMeasurements parseLatestMeasurements(String json) {
        LatestMeasurements latestMeasurements = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject result = results.getJSONObject(0);
            JSONArray measurementsArray = result.getJSONArray("measurements");


            latestMeasurements = new LatestMeasurements();
            latestMeasurements.setLocationName(result.getString("location"));

            ArrayList<Measurement> measurements = new ArrayList<>();
            for (int i = 0; i < measurementsArray.length(); i++) {
                JSONObject measurementResult = measurementsArray.getJSONObject(i);
                String parameter = measurementResult.getString("parameter");
                double value = measurementResult.getDouble("value");
                String unit = measurementResult.getString("unit");
                measurements.add(new Measurement(parameter, value, unit));
            }
            latestMeasurements.setMeasurements(measurements);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latestMeasurements;
    }

    class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnected(context)) {
                fetchLatestMeasurements(locationName);
            }
        }

        private boolean isConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            return (networkInfo != null && networkInfo.isConnected());
        }
    }
}
