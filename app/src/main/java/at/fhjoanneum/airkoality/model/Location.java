package at.fhjoanneum.airkoality.model;

public class Location {
    private String location;
    private String city;
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
