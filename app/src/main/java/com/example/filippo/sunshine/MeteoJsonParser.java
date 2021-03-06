package com.example.filippo.sunshine;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.app.Fragment;

import com.example.filippo.sunshine.model.MeteoInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by filippo on 05/03/2015.
 */
public class MeteoJsonParser {

    private static String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private static String formatHighLows(Fragment fragment, double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh;
        long roundedLow;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String unitType = sp.getString(fragment.getString(R.string.pref_units_key),fragment.getString(R.string.pref_location_default));

        if (unitType.equals(fragment.getString(R.string.pref_units_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.equals(fragment.getString(R.string.pref_units_metric))) {
            Log.d("sunshine", "Unit type not found: " + unitType);
        }

        roundedHigh = Math.round(high);
        roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public static MeteoInfo[] getWeatherDataFromJson(Fragment fragment, String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";
        final String OWM_ICON = "icon";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        Log.i("sunshine",forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        MeteoInfo[] results = new MeteoInfo[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;
            String icon;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);
            icon = weatherObject.getString(OWM_ICON);
            Log.i("sunshine","icon=" + icon);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(fragment, high, low);

            results[i] = new MeteoInfo();
            results[i].setDay(day);
            results[i].setDescription(description);
            results[i].setIcon(icon);
            results[i].setHighAndLow(highAndLow);
        }

        for (MeteoInfo s : results) {
            Log.i("sunshine", "Forecast entry: " + s.toString());
        }
        return results;

    }

}
