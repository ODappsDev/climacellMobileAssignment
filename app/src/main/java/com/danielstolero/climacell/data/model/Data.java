package com.danielstolero.climacell.data.model;

public class Data {

    private String time;
    private Value value;

    public Data(String time, Value value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
