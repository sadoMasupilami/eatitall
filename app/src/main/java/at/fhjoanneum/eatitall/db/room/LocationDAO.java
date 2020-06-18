package at.fhjoanneum.eatitall.db.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import at.fhjoanneum.eatitall.model.Location;

@Dao
public interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLocation(Location location);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<Location> locations);

    @Query("Select * from location")
    List<Location> getAll();

    @Query("Select * from location where location = :locationName")
    Location getLocationWithName(String locationName);

}
