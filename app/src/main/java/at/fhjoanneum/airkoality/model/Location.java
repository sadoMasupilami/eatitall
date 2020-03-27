package at.fhjoanneum.airkoality.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class Location {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "country")
    private String country;

    public Location(String location, String city, String country) {
        this.location = location;
        this.city = city;
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
