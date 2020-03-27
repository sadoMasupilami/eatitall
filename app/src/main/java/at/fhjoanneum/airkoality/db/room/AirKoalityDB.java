package at.fhjoanneum.airkoality.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import at.fhjoanneum.airkoality.model.Location;

@Database(entities = {Location.class}, version = 2)
public abstract class AirKoalityDB extends RoomDatabase {
    private static AirKoalityDB instance;

    public abstract LocationDAO locationDAO();

    public static AirKoalityDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AirKoalityDB.class, "airkoality").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
