package com.danielstolero.climacell.data.remote;

import android.support.annotation.NonNull;

import com.danielstolero.climacell.data.DataRepository;
import com.danielstolero.climacell.data.model.Country;
import com.danielstolero.climacell.data.model.Forecast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ApiHelper {

    private static ApiHelper instance;

    private Gson gson;
    private JsonParser parser;


    public static ApiHelper getInstance() {
        if (instance == null) {
            instance = new ApiHelper();
        }
        return instance;
    }

    private ApiHelper(){
        gson = new Gson().newBuilder().serializeNulls().create();
        parser = new JsonParser();
    }

    public void getCountries(final DataRepository repository) {
        HttpConnection.getInstance().get("https://restcountries.eu", "rest/v2/all", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    if (response.isSuccessful()) {
                        JsonArray jsonArray = parser.parse(responseStr).getAsJsonArray();
                        List<Country> data = Country.fromJsonArray(jsonArray.toString());
                        repository.setCountries(data);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

    public void getForecast(final DataRepository repository, final Country country) {

        Map<String, String> params = new HashMap<>();
        try {
            params.put("location_id", String.valueOf(country.getId()));
            params.put("lat", String.valueOf(country.getLocation().getLatitude()));
            params.put("lon", String.valueOf(country.getLocation().getLongitude()));
            params.put("num_days", String.valueOf(5));
            params.put("unit_system", "si");
            params.put("fields", "temp,precipitation");
        } catch (Exception ignore) {

        }

        HttpConnection.getInstance().get("https://api2.climacell.co", "v2/weather/forecast/daily", params, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseStr = response.body().string();
                    if (response.isSuccessful()) {
                        JsonArray jsonArray = parser.parse(responseStr).getAsJsonArray();

                        List<Forecast> data = Forecast.fromJsonArray(country, jsonArray.toString());
                        repository.setForecast(country, data);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

}
