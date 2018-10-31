package com.danielstolero.climacell.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "countries")
public class Country implements Comparable<Country> {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String capital;
    private String region;

    private Location location;
    private long population;
    private long area;

    private String flag;

    public Country(String name, String capital, String region, Location location, long population, long area, String flag) {
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.location = location;
        this.population = population;
        this.area = area;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public int compareTo(@NonNull Country o) {
        return 0;
    }

    public static List<Country> fromJsonArray(String jsonArray) {
        List<Country> countries = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonArray);
            for (int i = 0; i < data.length(); i++) {
                countries.add(fromJson(data.getJSONObject(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    private static Country fromJson(String jsonObject) throws JSONException {

        JSONObject data = new JSONObject(jsonObject);
        JSONArray location = data.getJSONArray("latlng");

        return new Country(
                data.getString("name"),
                data.getString("capital"),
                data.getString("region"),
                new Location(location.optDouble(0), location.optDouble(1)),
                data.optLong("population"),
                data.optLong("area"),
                data.getString("flag"));
    }
}
