package com.example.filippo.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by filippo on 02/03/2015.
 */
public class MeteoAsyncTask extends AsyncTask<String,Void,String> {

    IApiAccessResponse delegate=null;

    public interface IApiAccessResponse {
        void postResult(String asyncresult);
    }

    public MeteoAsyncTask(IApiAccessResponse delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        if(delegate!=null)
        {
            Log.i("sunshine","Result=" + result);
            delegate.postResult(result);
        }
        else
        {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }


    @Override
    protected String doInBackground(String... urls) {
        URL url = null;
        HttpURLConnection conn = null;
        String lastLine = null;

        try {
            // http://api.openweathermap.org/data/2.5/forecast?q=94043&mode=json&units=metric&cnt=7

            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";


            Uri myUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(FORMAT_PARAM,"json")
                    .appendQueryParameter(UNITS_PARAM,"metric")
                    .appendQueryParameter(DAYS_PARAM,"7")
                    .appendQueryParameter(QUERY_PARAM, urls[0])
                    .build();

            url = new URL(myUri.toString());
            Log.i("sunshine","Url String = " + url.toString());
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ( (line=reader.readLine())!=null) {
                //Log.i("sunshine", "Letto: " + line);
                lastLine = line;
            }
            } catch (Exception e) {
            e.printStackTrace();
            Log.e("sunshine",e.getMessage());
        } finally {
            if (conn!=null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("sunshine",e.getMessage());
                }
            }
        }
        return lastLine;
    }
}
