package com.danielstolero.climacell.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Value {

    private double value;
    private String units;

    public Value(double value, String units) {
        this.value = value;
        this.units = units;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Nullable
    public static Value fromJson(JSONObject jsonObject) {
        if (jsonObject != null)
            return new Value(jsonObject.optDouble("value"), jsonObject.optString("units"));
        else
            return null;
    }
}
