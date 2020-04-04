package at.fhjoanneum.airkoality.db.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import at.fhjoanneum.airkoality.model.LatestMeasurements;

@Dao
public interface LatestMeasurementDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(LatestMeasurements latestMeasurements);

    @Query("Select * from latestMeasurements where locationName = :locationName")
    LatestMeasurements getForLocation(String locationName);
}
