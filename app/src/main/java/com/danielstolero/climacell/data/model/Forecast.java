package com.danielstolero.climacell.data.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forecast {

    private int countryId;

    private String time;

    private Map<String, Data> temp;
    private Map<String, Data> precipitation;

    public Forecast(int countryId, String time, Map<String, Data> temp, Map<String, Data> precipitation) {
        this.countryId = countryId;
        this.time = time;
        this.temp = temp;
        this.precipitation = precipitation;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Data> getTemp() {
        return temp;
    }

    public void setTemp(Map<String, Data> temp) {
        this.temp = temp;
    }

    public Map<String, Data> getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Map<String, Data> precipitation) {
        this.precipitation = precipitation;
    }

    public static List<Forecast> fromJsonArray(Country country, String jsonArray) {
        List<Forecast> forecasts = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonArray);
            for (int i = 0; i < data.length(); i++) {
                forecasts.add(fromJson(country, data.getJSONObject(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecasts;
    }

    private static Forecast fromJson(Country country, String jsonObject) throws JSONException {

        JSONObject json = new JSONObject(jsonObject);

        // Load time.
        String time = json.getJSONObject("observation_time").getString("value");

        // Load temperature.
        JSONArray jTemp = json.getJSONArray("temp");
        JSONArray jPrecipitation = json.getJSONArray("precipitation");

        return new Forecast(country.getId(), time, loadData(jTemp), loadData(jPrecipitation));

    }

    private static Map<String, Data> loadData(JSONArray jsonArray) throws JSONException {
        Map<String, Data> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String time = object.getString("observation_time");
            Value minValue = Value.fromJson(object.optJSONObject("min"));
            if(minValue != null)
                map.put("min", new Data(time, minValue));

            Value maxValue = Value.fromJson(object.optJSONObject("max"));
            if(maxValue != null)
                map.put("max", new Data(time, maxValue));
        }

        return map;
    }
}
