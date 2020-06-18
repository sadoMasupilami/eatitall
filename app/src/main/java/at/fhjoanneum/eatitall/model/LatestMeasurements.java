package at.fhjoanneum.eatitall.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "latestMeasurements")
public class LatestMeasurements {
    @PrimaryKey
    @NonNull
    private String locationName;
    @ColumnInfo(name = "measurements")
    private ArrayList<Measurement> measurements;

    @NonNull
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(@NonNull String locationName) {
        this.locationName = locationName;
    }

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ArrayList<Measurement> measurements) {
        this.measurements = measurements;
    }
}
