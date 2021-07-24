package com.shakilsustswe.locationtracker;

public class LocationInfo {
    String Name;
    double latitude, langitude;

    public LocationInfo() {
    }

    public LocationInfo(String name, double latitude, double langitude) {
        Name = name;
        this.latitude = latitude;
        this.langitude = langitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }
}
