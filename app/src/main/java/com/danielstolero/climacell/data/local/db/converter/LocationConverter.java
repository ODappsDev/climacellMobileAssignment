package com.danielstolero.climacell.data.local.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.danielstolero.climacell.data.model.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LocationConverter {

    private static LocationConverter instance;

    private Gson gson;

    private static LocationConverter getInstance() {
        if (instance == null) {
            instance = new LocationConverter();
        }
        return instance;
    }

    public LocationConverter() {
        gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
    }

    @TypeConverter
    public Location storedStringToLanguages(String value) {
        return LocationConverter.getInstance().gson.fromJson(value, Location.class);
    }

    @TypeConverter
    public String languagesToStoredString(Location location) {
        return LocationConverter.getInstance().gson.toJson(location);
    }
}
