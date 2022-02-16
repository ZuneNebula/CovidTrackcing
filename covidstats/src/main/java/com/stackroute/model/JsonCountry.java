package com.stackroute.model;

public class JsonCountry {
    private String country;

    public JsonCountry() {
    }

    public JsonCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "JsonCountry{" +
                "country='" + country + '\'' +
                '}';
    }
}
