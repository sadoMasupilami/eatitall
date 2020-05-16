package at.fhjoanneum.airkoality.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import at.fhjoanneum.airkoality.model.LatestMeasurements;
import at.fhjoanneum.airkoality.model.Location;

@Database(entities = {Location.class, LatestMeasurements.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AirKoalityDB extends RoomDatabase {
    private static AirKoalityDB instance;

    public abstract LocationDAO locationDAO();

    public abstract LatestMeasurementDAO latestMeasurementDAO();

    public static AirKoalityDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AirKoalityDB.class, "airkoality").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
