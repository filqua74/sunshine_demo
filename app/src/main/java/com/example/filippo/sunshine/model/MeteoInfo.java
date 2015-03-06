package com.example.filippo.sunshine.model;

/**
 * Created by filippo on 06/03/2015.
 */
public class MeteoInfo {

    String day = null;
    String description = null;
    String highAndLow = null;
    String icon = null;

    public MeteoInfo() {
    }

    @Override
    public String toString() {
        return day + " - " + description + " - " + highAndLow;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighAndLow() {
        return highAndLow;
    }

    public void setHighAndLow(String highAndLow) {
        this.highAndLow = highAndLow;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }




}
