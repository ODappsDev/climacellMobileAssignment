package com.danielstolero.climacell.data.model;

public class Location {

    private double latitude;
    private double longitude;

    public Location (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location (int[] location) {
        this.latitude = location[0];
        this.longitude = location[1];
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
