package com.danielstolero.climacell.data.local.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.danielstolero.climacell.data.model.Forecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ForecastConverter {

    private static ForecastConverter instance;

    private Gson gson;

    private static ForecastConverter getInstance() {
        if (instance == null) {
            instance = new ForecastConverter();
        }
        return instance;
    }

    public ForecastConverter() {
        gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
    }

    @TypeConverter
    public Forecast storedStringToForecast(String value) {
        return ForecastConverter.getInstance().gson.fromJson(value, Forecast.class);
    }

    @TypeConverter
    public String forecastToStoredString(Forecast forecast) {
        return ForecastConverter.getInstance().gson.toJson(forecast);
    }
}
