package com.danielstolero.climacell.data.remote;

import android.support.annotation.NonNull;

import com.danielstolero.climacell.data.DataRepository;
import com.danielstolero.climacell.data.model.Country;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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
                        repository.setCountries(gson.fromJson(jsonArray, new TypeToken<List<Country>>(){}.getType()));
                    } else {

                    }
                } catch (Exception e) {

                }
            }
        }, false);
    }
}
